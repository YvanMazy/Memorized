package be.darkkraft.memorized.example.concurrent;

import be.darkkraft.memorized.data.counter.IntCounter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

import static be.darkkraft.memorized.example.concurrent.ConcurrentExampleServer.ACTIONS;

public class ActionThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionThread.class);

    private final CountDownLatch countDownLatch;
    private final IntCounter counter;

    public ActionThread(final @NotNull CountDownLatch countDownLatch, final @NotNull IntCounter counter, final int id) {
        super("Action Thread #" + id);
        this.countDownLatch = countDownLatch;
        this.counter = counter;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        try {
            final CompletableFuture<Integer>[] futures = new CompletableFuture[ACTIONS];
            // Obviously, it is preferable to add ACTIONS directly to the counters.
            for (int i = 0; i < ACTIONS; i++) {
                futures[i] = this.counter.asyncIncrementAndGet();
            }
            CompletableFuture.allOf(futures).join();
            this.countDownLatch.countDown();
        } catch (final Exception exception) {
            LOGGER.error("An exception occurred", exception);
        }
    }

}