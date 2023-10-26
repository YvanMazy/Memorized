package be.darkkraft.memorized.client.auth;

import be.darkkraft.memorized.packet.ByteBuf;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * An implementation of {@link AuthenticationInput} for token-based authentication.
 */
public final class TokenAuthenticationInput implements AuthenticationInput {

    private final String token;

    /**
     * Creates a new TokenAuthenticationInput with the specified token.
     *
     * @param token The authentication token.
     */
    @Contract(pure = true)
    public TokenAuthenticationInput(final String token) {
        this.token = token;
    }

    /**
     * Writes the authentication token into the provided ByteBuffer.
     *
     * @param buffer The {@link ByteBuffer} to write the authentication token into.
     */
    @Override
    public void write(final @NotNull ByteBuf buffer) {
        buffer.putString(this.token);
    }

}