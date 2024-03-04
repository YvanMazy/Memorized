package be.darkkraft.memorized.client.data;

import be.darkkraft.memorized.client.MemorizedClient;
import be.darkkraft.memorized.packet.ByteBuf;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * Provides an abstract base class for client accessors that require identifier handling.
 */
public abstract class IdentifiableClientAccessor extends ClientAccessor {

    protected int keyIdentifier = -1;

    /**
     * Creates a new IdentifiableClientAccessor with a given client.
     *
     * @param client The {@link MemorizedClient} associated with this accessor.
     */
    protected IdentifiableClientAccessor(final @NotNull MemorizedClient client) {
        super(client);
    }

    /**
     * Default constructor for IdentifiableClientAccessor.
     */
    protected IdentifiableClientAccessor() {
        // Default constructor
    }

    /**
     * Writes the identifier to the given {@link ByteBuffer}.
     *
     * @param buffer The buffer into which the identifier should be written.
     *
     * @return The modified {@link ByteBuffer} containing the identifier.
     */
    @NotNull
    protected ByteBuf writeId(final @NotNull ByteBuf buffer) {
        if (this.keyIdentifier == -1) {
            this.keyIdentifier = this.getKeyIdentifier(this.getKeyClass());
        }
        return this.client().getCodecRegistry().encode(buffer.putInt(this.keyIdentifier), this.getKeyId());
    }

    /**
     * Gets the key class for this accessor.
     *
     * @return The key class for this accessor.
     */
    @Contract(pure = true)
    protected abstract @NotNull Class<?> getKeyClass();

    /**
     * Gets the key identifier for this accessor.
     *
     * @return The key identifier for this accessor.
     */
    @Contract(pure = true)
    protected abstract @NotNull Object getKeyId();

}