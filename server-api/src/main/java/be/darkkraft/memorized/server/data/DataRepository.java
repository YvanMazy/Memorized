package be.darkkraft.memorized.server.data;

import be.darkkraft.memorized.server.data.container.DataContainer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * Interface for managing a data repository.
 *
 * @param <K> The type of key used for identifying data containers.
 *
 * @see DataContainer
 * @see ByteBuffer
 */
public interface DataRepository<K> {

    /**
     * Retrieves a {@link DataContainer} associated with the specified key.
     *
     * @param key The key used to look up the container.
     *
     * @return The {@link DataContainer} associated with the key.
     */
    DataContainer getContainer(final @NotNull K key);

    /**
     * Reads a key from a {@link ByteBuffer}.
     *
     * @param buffer The {@link ByteBuffer} to read from.
     *
     * @return The key read from the buffer.
     */
    @Nullable K readKey(final @NotNull ByteBuffer buffer);

    /**
     * Retrieves the identifier of the key.
     *
     * @return The identifier of the key.
     */
    @Contract(pure = true)
    int getKeyIdentifier();

    /**
     * Retrieves the class object representing the type of key.
     *
     * @return The {@link Class} object for the key.
     */
    @Contract(pure = true)
    @NotNull Class<K> getKeyClass();

}