package be.darkkraft.memorized.server.auth;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * An implementation of the Authenticator interface for unsecure authentication.
 */
public final class UnsecureAuthenticator implements Authenticator {

    /**
     * Authenticates a client using the provided channel and buffer. This implementation always returns true,
     * indicating unsecure authentication.
     *
     * @param channel The {@link SocketChannel} representing the client.
     * @param buffer  The {@link ByteBuffer} containing authentication data.
     *
     * @return Always returns true for unsecure authentication.
     */
    @Override
    public boolean auth(final @NotNull SocketChannel channel, final @NotNull ByteBuffer buffer) {
        return true;
    }

}