package be.darkkraft.memorized.client.data;

import be.darkkraft.memorized.client.MemorizedClient;
import be.darkkraft.memorized.client.exception.UnknownMemorizedClient;
import be.darkkraft.memorized.packet.ByteBuf;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;

/**
 * Provides an abstract base class for client accessors.
 * Client accessors are responsible for interacting with the {@link MemorizedClient}.
 */
public abstract class ClientAccessor {

    private MemorizedClient client;

    /**
     * Creates a new ClientAccessor with a given client.
     *
     * @param client The {@link MemorizedClient} associated with this accessor.
     */
    protected ClientAccessor(final @NotNull MemorizedClient client) {
        this.client = client;
    }

    /**
     * Default constructor for ClientAccessor.
     */
    protected ClientAccessor() {
        // Default constructor
    }

    /**
     * Updates the client associated with this accessor.
     *
     * @param client The new {@link MemorizedClient} to be associated with this accessor.
     */
    public void updateClient(final @NotNull MemorizedClient client) {
        this.client = client;
    }

    /**
     * Queues a transaction to the client's transaction queue.
     *
     * @param buffer The {@link ByteBuf} containing the data for the transaction.
     *
     * @return A {@link CompletableFuture} representing the result of the transaction.
     */
    @NotNull
    public CompletableFuture<ByteBuffer> queue(final @NotNull ByteBuf buffer) {
        return this.client().getTransactionQueue().queue(buffer);
    }

    /**
     * Writes data to the client's session.
     *
     * @param buffer The {@link ByteBuf} containing the data to be written.
     */
    public void write(final @NotNull ByteBuf buffer) {
        this.client().getSession().unsafeSend(buffer);
    }

    /**
     * Retrieves the key identifier for a given class from the client's key registry.
     *
     * @param keyClass The class for which to retrieve the key identifier.
     *
     * @return The key identifier.
     */
    protected int getKeyIdentifier(final @NotNull Class<?> keyClass) {
        return this.client.getKeyRegistry().getIdentifierFromClass(keyClass);
    }

    /**
     * Gets the client associated with this accessor.
     *
     * @return The {@link MemorizedClient} associated with this accessor.
     *
     * @throws UnknownMemorizedClient If the client is null.
     */
    @Contract(pure = true)
    @NotNull
    protected MemorizedClient client() {
        if (this.client == null) {
            throw new UnknownMemorizedClient();
        }
        return this.client;
    }

}