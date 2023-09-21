package be.darkkraft.memorized.example.concurrent;

import be.darkkraft.memorized.client.MemorizedClient;
import be.darkkraft.memorized.client.MemorizedClientBuilder;
import be.darkkraft.memorized.client.auth.AuthenticationInput;
import be.darkkraft.memorized.client.auth.TokenAuthenticationInput;
import be.darkkraft.memorized.client.data.counter.StringIntCounter;
import be.darkkraft.memorized.codec.registry.CodecRegistry;
import be.darkkraft.memorized.codec.registry.DefaultCodecRegistry;
import be.darkkraft.memorized.data.counter.IntCounter;
import be.darkkraft.memorized.data.key.ClassKeyRegistry;
import be.darkkraft.memorized.data.key.KeyRegistry;
import be.darkkraft.memorized.server.MemorizedServer;
import be.darkkraft.memorized.server.MemorizedServerBuilder;
import be.darkkraft.memorized.server.auth.Authenticator;
import be.darkkraft.memorized.server.auth.TokenAuthenticator;
import be.darkkraft.memorized.server.data.DataRepositoryCoordinator;
import be.darkkraft.memorized.server.data.StringDataRepository;
import be.darkkraft.memorized.server.data.counter.ServerIntCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ConcurrentExampleServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConcurrentExampleServer.class);

    public static final int CLIENTS = 10;
    public static final int THREADS = 100;
    public static final int ACTIONS = 100;
    public static final String COUNTER_ID = "my-counter";

    public static final int EXPECTED_RESULT = CLIENTS * THREADS * ACTIONS;

    public static void main(final String[] args) throws InterruptedException {
        final InetSocketAddress address = new InetSocketAddress("127.0.0.1", 12345);
        final String token = "my-secure-token";
        final Authenticator authenticator = new TokenAuthenticator(token);
        final AuthenticationInput authenticationInput = new TokenAuthenticationInput(token);
        final CodecRegistry codecRegistry = new DefaultCodecRegistry().registerDefaults();
        final KeyRegistry<Class<?>> keyRegistry = new ClassKeyRegistry().register(String.class, 0);
        final StringDataRepository repository = new StringDataRepository(codecRegistry);
        final DataRepositoryCoordinator dataRepositoryCoordinator = new DataRepositoryCoordinator().register(repository);

        final MemorizedServer server = new MemorizedServerBuilder().address(address)
                .workerThreads(1)
                .authenticator(authenticator)
                .codecRegistry(codecRegistry)
                .dataRepositoryCoordinator(dataRepositoryCoordinator)
                .build();
        server.start();

        repository.register(COUNTER_ID, new ServerIntCounter());

        final List<MemorizedClient> clients = new ArrayList<>();
        for (int k = 0; k < CLIENTS; k++) {
            final MemorizedClient client = new MemorizedClientBuilder().serverAddress(address)
                    .authenticationInput(authenticationInput)
                    .codecRegistry(codecRegistry)
                    .keyRegistry(keyRegistry)
                    .build();
            clients.add(client);
            client.start();
        }

        final long start = System.currentTimeMillis();
        final CountDownLatch countDownLatch = new CountDownLatch(clients.size());

        for (int i = 0; i < clients.size(); i++) {
            new ClientCounterThread(clients.get(i), countDownLatch, i).start();
        }

        countDownLatch.await();

        final MemorizedClient randomClient = clients.get(0);
        final IntCounter counter = new StringIntCounter(randomClient, COUNTER_ID);

        LOGGER.info("Clients: {} Threads: {} Actions: {}", CLIENTS, THREADS, ACTIONS);
        LOGGER.info("Expected: {} Result: {} Use: {}ms", EXPECTED_RESULT, counter.blockingGet(), System.currentTimeMillis() - start);

        for (final MemorizedClient client : clients) {
            client.shutdown();
        }

        server.shutdown();
    }

}