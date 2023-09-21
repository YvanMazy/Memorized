package be.darkkraft.memorized.client.auth;

import be.darkkraft.memorized.client.MemorizedClient;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * An interface representing authentication input for a {@link MemorizedClient}.
 */
public interface AuthenticationInput {

    /**
     * Writes authentication data into the provided {@link ByteBuffer}.
     *
     * @param buffer The {@link ByteBuffer} to write authentication data into.
     */
    void write(final @NotNull ByteBuffer buffer);

}