package be.darkkraft.memorized.client.net;

import be.darkkraft.memorized.client.exception.SessionNotOpenException;
import be.darkkraft.memorized.packet.ByteBuf;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;

/**
 * Defines the contract for a transaction queue to manage client-server transactions.
 */
public interface TransactionQueue {

    /**
     * Informs that the session is ready to receive data.
     */
    @ApiStatus.Internal
    void onSessionReady();

    /**
     * Completes a transaction by processing the incoming {@link ByteBuffer}.
     *
     * @param buffer The buffer containing the incoming data.
     */
    void complete(@Nullable ByteBuffer buffer);

    /**
     * Queues a transaction to be sent to the server.
     *
     * @param buffer The buffer containing the outgoing data.
     *
     * @return A {@link CompletableFuture} representing the result of the transaction.
     *
     * @throws SessionNotOpenException If the session is not open
     */
    CompletableFuture<ByteBuffer> queue(@NotNull ByteBuf buffer);

    /**
     * Queues a packet to be sent to the server.
     *
     * @param buffer The buffer containing the outgoing data.
     *
     * @throws SessionNotOpenException If the session is not open
     */
    void directQueue(@NotNull ByteBuf buffer);

    /**
     * Retrieves the current size of the transaction queue.
     *
     * @return The number of pending transactions in the queue.
     */
    int size();

    /**
     * Checks if the transaction queue is empty.
     *
     * @return True if the queue is empty, false otherwise.
     */
    boolean isEmpty();

}