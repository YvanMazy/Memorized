package be.darkkraft.memorized.client.auth;

import be.darkkraft.memorized.client.MemorizedClient;
import be.darkkraft.memorized.packet.ByteBuf;
import org.jetbrains.annotations.NotNull;

/**
 * An interface representing authentication input for a {@link MemorizedClient}.
 */
public interface AuthenticationInput {

    /**
     * Writes authentication data into the provided {@link ByteBuf}.
     *
     * @param buffer The {@link ByteBuf} to write authentication data into.
     */
    void write(final @NotNull ByteBuf buffer);

}