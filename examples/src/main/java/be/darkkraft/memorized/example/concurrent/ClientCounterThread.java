package be.darkkraft.memorized.example.concurrent;

import be.darkkraft.memorized.client.MemorizedClient;
import be.darkkraft.memorized.client.data.counter.StringIntCounter;
import be.darkkraft.memorized.data.counter.IntCounter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

import static be.darkkraft.memorized.example.concurrent.ConcurrentExampleServer.COUNTER_ID;
import static be.darkkraft.memorized.example.concurrent.ConcurrentExampleServer.THREADS;

public class ClientCounterThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientCounterThread.class);

    private final MemorizedClient client;
    private final CountDownLatch countDownLatch;
    private final int id;

    public ClientCounterThread(final @NotNull MemorizedClient client, final @NotNull CountDownLatch countDownLatch, final int id) {
        super("Client Counter Thread #" + id);
        this.client = client;
        this.countDownLatch = countDownLatch;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            final IntCounter counter = new StringIntCounter(this.client, COUNTER_ID);

            final CountDownLatch subCountDownLatch = new CountDownLatch(THREADS);
            for (int i = 0; i < THREADS; i++) {
                new ActionThread(subCountDownLatch, counter, (this.id * THREADS) + i).start();
            }
            subCountDownLatch.await();
            this.countDownLatch.countDown();
        } catch (final Exception exception) {
            LOGGER.error("An exception occurred", exception);
        }
    }

}
