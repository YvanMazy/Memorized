package be.darkkraft.memorized.server.auth;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Objects;

/**
 * An implementation of the {@link Authenticator} interface for token-based authentication.
 */
public final class TokenAuthenticator implements Authenticator {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenAuthenticator.class);

    private final byte[] token;

    /**
     * Creates a new TokenAuthenticator with the specified token.
     *
     * @param token The authentication token.
     */
    public TokenAuthenticator(final String token) {
        this(token.getBytes());
    }

    /**
     * Creates a new TokenAuthenticator with the specified token.
     *
     * @param token The authentication token.
     */
    public TokenAuthenticator(final byte[] token) {
        this.token = Objects.requireNonNull(token, "Token cannot be null");
    }

    /**
     * Authenticates a client using the provided channel and buffer.
     *
     * @param channel The {@link SocketChannel} representing the client.
     * @param buffer  The {@link ByteBuffer} containing authentication data.
     *
     * @return True if authentication is successful, false otherwise.
     */
    @Override
    public boolean auth(final @NotNull SocketChannel channel, final @NotNull ByteBuffer buffer) {
        final int length = buffer.getInt();
        if (length != this.token.length || length > 384) {
            return false;
        }
        final byte[] bytes = new byte[length];
        buffer.get(bytes);
        return Arrays.equals(this.token, bytes);
    }

}