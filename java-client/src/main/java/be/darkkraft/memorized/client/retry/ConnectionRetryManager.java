package be.darkkraft.memorized.client.retry;

import be.darkkraft.memorized.client.MemorizedClientImpl;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConnectionRetryManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionRetryManager.class);

    private final MemorizedClientImpl client;
    private ScheduledExecutorService executor;

    public ConnectionRetryManager(final @NotNull MemorizedClientImpl client) {
        this.client = Objects.requireNonNull(client, "Client cannot be null");
    }

    public void start() {
        if (this.executor != null) {
            // Already started?
            return;
        }
        this.executor = Executors.newSingleThreadScheduledExecutor();
        final long delay = this.client.getConfiguration().connectionRetryDelay();
        this.executor.scheduleWithFixedDelay(this::retry, delay, delay, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        if (this.executor != null) {
            this.executor.shutdownNow();
            this.executor = null;
        }
    }

    private void retry() {
        LOGGER.info("Attempting to reconnect with the server...");
        if (!this.client.connect()) {
            this.stop();
        }
    }

}