package be.darkkraft.memorized.client.data;

import be.darkkraft.memorized.client.MemorizedClient;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * Provides an abstract base class for client accessors that require identifier handling.
 */
public abstract class IdentifiableClientAccessor extends ClientAccessor {

    /**
     * Creates a new IdentifiableClientAccessor with a given client.
     *
     * @param client The {@link MemorizedClient} associated with this accessor.
     */
    public IdentifiableClientAccessor(final @NotNull MemorizedClient client) {
        super(client);
    }

    /**
     * Default constructor for IdentifiableClientAccessor.
     */
    public IdentifiableClientAccessor() {
        // Default constructor
    }

    /**
     * Writes the identifier to the given {@link ByteBuffer}.
     *
     * @param buffer The buffer into which the identifier should be written.
     * @return The modified {@link ByteBuffer} containing the identifier.
     */
    @NotNull
    protected abstract ByteBuffer writeId(@NotNull ByteBuffer buffer);

}