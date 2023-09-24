package be.darkkraft.memorized.example.distributed;

import java.util.concurrent.CompletableFuture;

public interface CommonBalance {

    String id();

    default double balance() {
        return this.asyncBalance().join();
    }

    CompletableFuture<Double> asyncBalance();

    void give(final double amount);

    default boolean tryToTake(final double amount) {
        return this.asyncTryToTake(amount).join();
    }

    CompletableFuture<Boolean> asyncTryToTake(final double amount);

}