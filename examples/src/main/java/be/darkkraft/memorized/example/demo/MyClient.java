package be.darkkraft.memorized.example.demo;

import be.darkkraft.memorized.client.MemorizedClient;
import be.darkkraft.memorized.client.MemorizedClientBuilder;
import be.darkkraft.memorized.client.auth.UnsecureAuthenticationInput;
import be.darkkraft.memorized.codec.registry.DefaultCodecRegistry;
import be.darkkraft.memorized.data.key.ClassKeyRegistry;

import java.net.InetSocketAddress;

public class MyClient {

    public static void main(final String[] args) {
        final MemorizedClient client = new MemorizedClientBuilder().serverAddress(new InetSocketAddress("127.0.0.1", 12345))
                .authenticationInput(new UnsecureAuthenticationInput())
                .codecRegistry(new DefaultCodecRegistry().registerDefaults())
                .keyRegistry(new ClassKeyRegistry())
                .build();
        client.start();


    }

}