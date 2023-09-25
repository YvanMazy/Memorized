package be.darkkraft.memorized.example.map;

import be.darkkraft.memorized.client.MemorizedClient;
import be.darkkraft.memorized.client.MemorizedClientBuilder;
import be.darkkraft.memorized.client.auth.AuthenticationInput;
import be.darkkraft.memorized.client.auth.TokenAuthenticationInput;
import be.darkkraft.memorized.client.data.map.StringMemorizedMap;
import be.darkkraft.memorized.codec.registry.CodecRegistry;
import be.darkkraft.memorized.codec.registry.DefaultCodecRegistry;
import be.darkkraft.memorized.data.key.ClassKeyRegistry;
import be.darkkraft.memorized.data.map.MemorizedMap;
import be.darkkraft.memorized.example.simple.Product;
import be.darkkraft.memorized.example.simple.codec.ProductCodec;
import be.darkkraft.memorized.server.MemorizedServer;
import be.darkkraft.memorized.server.MemorizedServerBuilder;
import be.darkkraft.memorized.server.auth.Authenticator;
import be.darkkraft.memorized.server.auth.TokenAuthenticator;
import be.darkkraft.memorized.server.data.DataRepositoryCoordinator;
import be.darkkraft.memorized.server.data.StringDataRepository;
import be.darkkraft.memorized.server.data.map.ServerMemorizedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class MapPerformanceExample {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapPerformanceExample.class);

    private static final int ACTIONS = 100_000;

    public static void main(final String[] args) {
        final InetSocketAddress address = new InetSocketAddress("127.0.0.1", 12345);
        final Authenticator authenticator = new TokenAuthenticator("my-secure-token");
        final AuthenticationInput authenticationInput = new TokenAuthenticationInput("my-secure-token");
        final CodecRegistry codecRegistry = new DefaultCodecRegistry().registerDefaults().register(Product.class, new ProductCodec());

        final StringDataRepository repository = new StringDataRepository(codecRegistry);
        final DataRepositoryCoordinator dataRepositoryCoordinator = new DataRepositoryCoordinator().register(repository);

        final MemorizedServer server = new MemorizedServerBuilder().address(address)
                .workerThreads(1)
                .authenticator(authenticator)
                .codecRegistry(codecRegistry)
                .dataRepositoryCoordinator(dataRepositoryCoordinator)
                .build();
        server.start();

        repository.register("my-map", new ServerMemorizedMap<>(server, String.class, String.class));

        final MemorizedClient client = new MemorizedClientBuilder().serverAddress(address)
                .authenticationInput(authenticationInput)
                .codecRegistry(codecRegistry)
                .keyRegistry(new ClassKeyRegistry().register(String.class, 0))
                .build();
        client.start();

        final MemorizedMap<String, String> productMap = new StringMemorizedMap<>(client, String.class, "my-map");
        final long start = System.currentTimeMillis();
        for (int i = 0; i < ACTIONS; i++) {
            productMap.put("my-key", "test");
            productMap.remove("my-key");
        }

        LOGGER.info("Duration {}ms valid={}", System.currentTimeMillis() - start, productMap.blockingGet("my-key") == null);

        client.shutdown();
        server.shutdown();
    }

}