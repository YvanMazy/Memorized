package be.darkkraft.memorized.client.packet.command;

import be.darkkraft.memorized.client.MemorizedClient;
import be.darkkraft.memorized.client.exception.SessionNotOpenException;
import be.darkkraft.memorized.client.net.TransactionQueue;
import be.darkkraft.memorized.net.session.Session;
import be.darkkraft.memorized.packet.ByteBuf;
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
    private final Queue<ByteBuf> pendingBuffers = new ConcurrentLinkedQueue<>();

    @NotNull
    private final MemorizedClient client;

    /**
     * Constructs a new {@link TransactionQueueImpl} instance associated with the given {@link MemorizedClient}.
     *
     * @param client The {@link MemorizedClient} with which the transaction queue is associated.
     */
    public TransactionQueueImpl(final @NotNull MemorizedClient client) {
        this.client = client;
    }

    @Override
    public void onSessionReady() {
        while (!this.pendingBuffers.isEmpty()) {
            this.directQueue(this.pendingBuffers.poll());
        }
    }

    /**
     * Completes the current transaction with the given buffer.
     *
     * @param buffer The {@link ByteBuf} containing the result of the transaction, can be null.
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
     * @param buffer The {@link ByteBuf} containing the transaction data.
     *
     * @return A {@link CompletableFuture} representing the future result of the transaction.
     *
     * @throws SessionNotOpenException If the session is not open
     */
    @NotNull
    @Override
    public CompletableFuture<ByteBuffer> queue(final @NotNull ByteBuf buffer) {
        final CompletableFuture<ByteBuffer> future = new CompletableFuture<>();
        this.futures.add(future);
        this.directQueue(buffer);
        return future;
    }

    @Override
    public void directQueue(final @NotNull ByteBuf buffer) {
        try {
            final Session session = this.client.getSession();
            if (session == null) {
                throw new SessionNotOpenException();
            }
            Session.send(session.getChannel(), buffer);
            buffer.getBuffer().clear();
        } catch (final Exception exception) {
            LOGGER.error("Failed to queue a buffer", exception);
            this.pendingBuffers.add(buffer);
        }
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