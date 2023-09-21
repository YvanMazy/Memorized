package be.darkkraft.memorized.client.auth;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * An implementation of {@link AuthenticationInput} for unsecure authentication.
 */
public final class UnsecureAuthenticationInput implements AuthenticationInput {

    /**
     * Writes authentication data into the provided ByteBuffer. This implementation does nothing, as unsecure
     * authentication does not require additional data.
     *
     * @param buffer The {@link ByteBuffer} to write authentication data into.
     */
    @Override
    public void write(final @NotNull ByteBuffer buffer) {
        // No additional data required for unsecure authentication
    }

}