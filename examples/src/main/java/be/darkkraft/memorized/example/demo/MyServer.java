package be.darkkraft.memorized.example.demo;

import be.darkkraft.memorized.codec.registry.DefaultCodecRegistry;
import be.darkkraft.memorized.server.MemorizedServer;
import be.darkkraft.memorized.server.MemorizedServerBuilder;
import be.darkkraft.memorized.server.auth.UnsecureAuthenticator;
import be.darkkraft.memorized.server.data.DataRepositoryCoordinator;

import java.net.InetSocketAddress;

public class MyServer {

    public static void main(final String[] args) {
        final MemorizedServer server = new MemorizedServerBuilder().address(new InetSocketAddress("127.0.0.1", 12345))
                .workerThreads(1)
                .authenticator(new UnsecureAuthenticator())
                .codecRegistry(new DefaultCodecRegistry().registerDefaults())
                .dataRepositoryCoordinator(new DataRepositoryCoordinator())
                .build();
        server.start();
    }

}