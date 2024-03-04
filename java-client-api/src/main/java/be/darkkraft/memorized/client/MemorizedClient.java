package be.darkkraft.memorized.client;

import be.darkkraft.memorized.client.auth.AuthenticationInput;
import be.darkkraft.memorized.client.config.ClientConfiguration;
import be.darkkraft.memorized.client.net.TransactionQueue;
import be.darkkraft.memorized.codec.registry.CodecRegistry;
import be.darkkraft.memorized.data.key.KeyRegistry;
import be.darkkraft.memorized.net.session.Session;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

/**
 * Defines the contract for a Memorized client.
 */
public interface MemorizedClient {

    /**
     * Starts the client, initiating the connection to the server.
     */
    void start();

    /**
     * Shuts down the client, terminating any existing connections and resources.
     */
    void shutdown();

    /**
     * Handles an incoming packet from the server.
     *
     * @param buffer The packet data encapsulated in a {@link ByteBuffer}.
     *
     * @return True if the packet was successfully handled, false otherwise.
     *
     * @throws IOException If an I/O exception occurs while handling the packet.
     */
    boolean handlePacket(@NotNull ByteBuffer buffer) throws IOException;

    /**
     * Retrieves the server's address.
     *
     * @return The {@link InetSocketAddress} of the server to which the client is connected.
     */
    @Contract(pure = true)
    @NotNull InetSocketAddress getServerAddress();

    /**
     * Retrieves the client's authentication details.
     *
     * @return The {@link AuthenticationInput} used for client authentication.
     */
    @Contract(pure = true)
    @NotNull AuthenticationInput getAuthenticationInput();

    /**
     * Retrieves the codec registry used for serialization and deserialization.
     *
     * @return The {@link CodecRegistry} used by the client.
     */
    @Contract(pure = true)
    @NotNull CodecRegistry getCodecRegistry();

    /**
     * Retrieves the key registry used for data storage and retrieval.
     *
     * @return The {@link KeyRegistry} used by the client.
     */
    @Contract(pure = true)
    @NotNull KeyRegistry<Class<?>> getKeyRegistry();

    /**
     * Retrieves the transaction queue for handling server transactions.
     *
     * @return The {@link TransactionQueue} used by the client.
     */
    TransactionQueue getTransactionQueue();

    /**
     * Retrieves the session representing the connection to the server.
     *
     * @return The {@link Session} representing the current server connection.
     */
    Session getSession();

    /**
     * Checks if the client is currently running.
     *
     * @return True if the client is running, false otherwise.
     */
    @Contract(pure = true)
    boolean isRunning();

    /**
     * Gets the size limit of a received packet.
     *
     * @param authenticated true if the session is authenticated.
     *
     * @return The packet size.
     */
    @Contract(pure = true)
    default int getPacketSizeLimit(final boolean authenticated) {
        final ClientConfiguration config = this.getConfiguration();
        return authenticated ? config.packetSizeLimit() : config.unauthenticatedPacketSizeLimit();
    }

    /**
     * Gets the client configuration.
     *
     * @return The client configuration.
     */
    @Contract(pure = true)
    @NotNull ClientConfiguration getConfiguration();

    /**
     * Starts the automatic reconnection system.
     */
    @ApiStatus.Internal
    void tryToReconnect();

}