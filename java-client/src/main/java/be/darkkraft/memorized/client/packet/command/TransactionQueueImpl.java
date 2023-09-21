package be.darkkraft.memorized.client.packet.command;

import be.darkkraft.memorized.client.MemorizedClient;
import be.darkkraft.memorized.client.net.TransactionQueue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Implementation of the {@link TransactionQueue} interface.
 */
public class TransactionQueueImpl implements TransactionQueue {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionQueueImpl.class);

    @NotNull
    private final Queue<CompletableFuture<ByteBuffer>> futures = new ConcurrentLinkedQueue<>();

    @NotNull
    private final MemorizedClient client;

    /**
     * Constructs a new {@link TransactionQueueImpl} instance associated with the given {@link MemorizedClient}.
     *
     * @param client The {@link MemorizedClient} with which the transaction queue is associated.
     */
    public TransactionQueueImpl(@NotNull final MemorizedClient client) {
        this.client = client;
    }

    /**
     * Completes the current transaction with the given buffer.
     *
     * @param buffer The {@link ByteBuffer} containing the result of the transaction, can be null.
     */
    @Override
    public void complete(final @Nullable ByteBuffer buffer) {
        final CompletableFuture<ByteBuffer> future = this.futures.poll();
        if (future == null) {
            LOGGER.warn("Received a packet without any request!");
            return;
        }
        future.complete(buffer);
    }

    /**
     * Queues a new transaction into the transaction queue.
     *
     * @param buffer The {@link ByteBuffer} containing the transaction data.
     * @return A {@link CompletableFuture} representing the future result of the transaction.
     */
    @NotNull
    @Override
    public CompletableFuture<ByteBuffer> queue(final @NotNull ByteBuffer buffer) {
        final CompletableFuture<ByteBuffer> future = new CompletableFuture<>();
        this.futures.add(future);
        this.client.getSession().unsafeSend(buffer);
        return future;
    }

    /**
     * Returns the number of pending transactions in the queue.
     *
     * @return The number of pending transactions.
     */
    @Override
    @Contract(pure = true)
    public int size() {
        return this.futures.size();
    }

    /**
     * Checks if the transaction queue is empty.
     *
     * @return True if the queue is empty, false otherwise.
     */
    @Override
    @Contract(pure = true)
    public boolean isEmpty() {
        return this.futures.isEmpty();
    }

}