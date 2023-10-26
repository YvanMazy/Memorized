package be.darkkraft.memorized.server.data;

import be.darkkraft.memorized.server.data.container.DataContainer;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * Implementation of {@link DataRepository} that uses Byte as the key type.
 * Manages data containers and provides methods for key serialization and deserialization.
 */
public final class ByteDataRepository implements DataRepository<Byte> {

    private final Byte2ObjectMap<DataContainer> containers = new Byte2ObjectOpenHashMap<>();

    /**
     * Registers a data container with the given key.
     *
     * @param key       The key to associate with the data container.
     * @param container The data container to register.
     */
    public void register(final byte key, final @NotNull DataContainer container) {
        this.containers.put(key, container);
    }

    /**
     * Retrieves the data container associated with the given key.
     *
     * @param key The key whose associated data container is to be returned.
     *
     * @return The data container associated with the specified key, or {@code null} if no container is found.
     */
    @Override
    public DataContainer getContainer(final @NotNull Byte key) {
        return this.containers.get(key.byteValue());
    }

    /**
     * Reads and returns the key from the given buffer.
     *
     * @param buffer The buffer from which to read the key.
     *
     * @return The key, or {@code null} if the key cannot be decoded.
     */
    @Override
    public Byte readKey(final @NotNull ByteBuffer buffer) {
        return buffer.get();
    }

    /**
     * Gets the class type of the key used in this repository.
     *
     * @return The class type of the key.
     */
    @Override
    public @NotNull Class<Byte> getKeyClass() {
        return byte.class;
    }

    /**
     * Gets the identifier for the key type.
     *
     * @return The identifier for the key type.
     */
    @Override
    public int getKeyIdentifier() {
        return 1;
    }

}