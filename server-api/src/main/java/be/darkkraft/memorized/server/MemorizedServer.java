package be.darkkraft.memorized.server;

import be.darkkraft.memorized.codec.registry.CodecRegistry;
import be.darkkraft.memorized.net.session.Session;
import be.darkkraft.memorized.server.auth.Authenticator;
import be.darkkraft.memorized.server.data.DataRepositoryCoordinator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Collection;

/**
 * Interface defining the operations of a Memorized Server.
 */
public interface MemorizedServer {

    /**
     * Starts the Memorized Server.
     */
    void start();

    /**
     * Shuts down the Memorized Server.
     */
    void shutdown();

    /**
     * Handles a new session initiated by a client.
     *
     * @param client The client's {@link SocketChannel}.
     */
    void handleNewSession(final @NotNull SocketChannel client);

    /**
     * Handles the removal of a session.
     *
     * @param client The client's {@link SocketChannel}.
     */
    void handleRemoveSession(final @NotNull SocketChannel client);

    /**
     * Handles a packet received from a client.
     *
     * @param client The client's {@link SocketChannel}.
     * @param buffer The {@link ByteBuffer} containing the packet data.
     * @return {@code true} if the packet was handled successfully, {@code false} otherwise.
     * @throws IOException If an I/O error occurs.
     */
    boolean handlePacket(final @NotNull SocketChannel client, final @NotNull ByteBuffer buffer) throws IOException;

    /**
     * Retrieves the {@link Session} associated with a socket channel.
     *
     * @param channel The {@link SocketChannel} to look up.
     * @return The associated {@link Session}.
     */
    @Contract(pure = true)
    @Nullable Session getSession(final @NotNull SocketChannel channel);

    /**
     * Retrieves all active {@link Session Sessions}.
     *
     * @return A {@link Collection} of active {@link Session Sessions}.
     */
    @Contract(pure = true)
    @NotNull Collection<Session> getSessions();

    /**
     * Gets the server's {@link InetSocketAddress}.
     *
     * @return The server's address.
     */
    @Contract(pure = true)
    @NotNull InetSocketAddress getAddress();

    /**
     * Gets the {@link Authenticator} used by the server.
     *
     * @return The authenticator used by the server.
     */
    @Contract(pure = true)
    @NotNull Authenticator getAuthenticator();

    /**
     * Gets the {@link CodecRegistry} used by the server.
     *
     * @return The codec registry.
     */
    @Contract(pure = true)
    @NotNull CodecRegistry getCodecRegistry();

    /**
     * Gets the {@link DataRepositoryCoordinator} used by the server.
     *
     * @return The data repository coordinator.
     */
    @Contract(pure = true)
    @NotNull DataRepositoryCoordinator getDataRepositoryCoordinator();

    /**
     * Gets the number of worker threads used by the server.
     *
     * @return The number of worker threads.
     */
    @Contract(pure = true)
    int getWorkerThreads();

    /**
     * Checks if the server is running.
     *
     * @return {@code true} if the server is running, {@code false} otherwise.
     */
    @Contract(pure = true)
    boolean isRunning();

}