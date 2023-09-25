package be.darkkraft.memorized.example.distributed;

import be.darkkraft.memorized.client.MemorizedClient;
import be.darkkraft.memorized.client.MemorizedClientBuilder;
import be.darkkraft.memorized.client.auth.AuthenticationInput;
import be.darkkraft.memorized.client.auth.TokenAuthenticationInput;
import be.darkkraft.memorized.codec.registry.CodecRegistry;
import be.darkkraft.memorized.codec.registry.DefaultCodecRegistry;
import be.darkkraft.memorized.data.key.ClassKeyRegistry;
import be.darkkraft.memorized.data.key.KeyRegistry;
import be.darkkraft.memorized.example.simple.Product;
import be.darkkraft.memorized.example.simple.codec.ProductCodec;
import be.darkkraft.memorized.server.MemorizedServer;
import be.darkkraft.memorized.server.MemorizedServerBuilder;
import be.darkkraft.memorized.server.auth.Authenticator;
import be.darkkraft.memorized.server.auth.TokenAuthenticator;
import be.darkkraft.memorized.server.data.DataRepositoryCoordinator;
import be.darkkraft.memorized.server.data.StringDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CustomDistributed {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomDistributed.class);
    private static final int CLIENTS = 20;

    public static void main(final String[] args) throws InterruptedException, BrokenBarrierException {
        final InetSocketAddress address = new InetSocketAddress("127.0.0.1", 12345);
        final Authenticator authenticator = new TokenAuthenticator("my-secure-token");
        final AuthenticationInput authenticationInput = new TokenAuthenticationInput("my-secure-token");
        final KeyRegistry<Class<?>> keyRegistry = new ClassKeyRegistry().register(String.class, 0);
        final CodecRegistry codecRegistry = new DefaultCodecRegistry().registerDefaults().register(Product.class, new ProductCodec());

        final StringDataRepository repository = new StringDataRepository(codecRegistry);
        final DataRepositoryCoordinator dataRepositoryCoordinator = new DataRepositoryCoordinator().register(repository);

        final MemorizedServer server = new MemorizedServerBuilder().address(address)
                .workerThreads(CLIENTS)
                .authenticator(authenticator)
                .codecRegistry(codecRegistry)
                .dataRepositoryCoordinator(dataRepositoryCoordinator)
                .build();
        server.start();

        repository.register("my-common-balance", new ServerCommonBalance());

        final MemorizedClient[] clients = new MemorizedClient[CLIENTS];
        for (int i = 0; i < CLIENTS; i++) {
            final MemorizedClient client = new MemorizedClientBuilder().serverAddress(address)
                    .authenticationInput(authenticationInput)
                    .codecRegistry(codecRegistry)
                    .keyRegistry(keyRegistry)
                    .build();
            clients[i] = client;
            client.start();
        }

        final CyclicBarrier barrier = new CyclicBarrier(CLIENTS + 1);
        for (int i = 0; i < CLIENTS; i++) {
            new ActionThread(barrier, new ClientCommonBalance(clients[i], "my-common-balance"), i).start();
        }

        barrier.await();
        barrier.await();

        final CommonBalance balance = new ClientCommonBalance(clients[0], "my-common-balance");
        LOGGER.info("Expected {} Result {}", CLIENTS * 5, balance.balance());

        for (final MemorizedClient client : clients) {
            client.shutdown();
        }
        server.shutdown();
    }

}