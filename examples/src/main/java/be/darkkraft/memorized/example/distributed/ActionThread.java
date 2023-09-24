package be.darkkraft.memorized.example.distributed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CyclicBarrier;

public class ActionThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionThread.class);

    private final CyclicBarrier barrier;
    private final CommonBalance balance;

    public ActionThread(final CyclicBarrier barrier, final CommonBalance balance, final int id) {
        super("Action Thread #" + id);
        this.barrier = barrier;
        this.balance = balance;
    }

    @Override
    public void run() {
        try {
            this.barrier.await();
            LOGGER.info("Current balance: {}", this.balance.balance());
            this.balance.give(10);
            LOGGER.info("Can take 5: {}", this.balance.tryToTake(5));
            LOGGER.info("Current balance: {}", this.balance.balance());
            this.barrier.await();
        } catch (final Exception exception) {
            LOGGER.error("An exception occurred", exception);
        }
    }

}