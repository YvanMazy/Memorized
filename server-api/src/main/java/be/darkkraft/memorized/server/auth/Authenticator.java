package be.darkkraft.memorized.server.auth;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * An interface representing an authenticator for the server.
 */
public interface Authenticator {

    /**
     * Authenticates a client using the provided channel and buffer.
     *
     * @param channel The socket channel representing the client.
     * @param buffer  The {@link ByteBuffer} containing authentication data.
     *
     * @return True if authentication is successful, false otherwise.
     */
    boolean auth(@NotNull SocketChannel channel, @NotNull ByteBuffer buffer);

}