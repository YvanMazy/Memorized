package be.darkkraft.memorized.server.data;

import be.darkkraft.memorized.codec.registry.CodecRegistry;
import be.darkkraft.memorized.server.data.container.DataContainer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Implementation of {@link DataRepository} that uses String as the key type.
 * Manages data containers and provides methods for key serialization and deserialization.
 */
public final class StringDataRepository implements DataRepository<String> {

    private final CodecRegistry codecRegistry;

    private final Map<String, DataContainer> containers = new HashMap<>();

    /**
     * Constructs a new {@link StringDataRepository} with the given {@link CodecRegistry}.
     *
     * @param codecRegistry The codec registry for encoding and decoding keys.
     */
    @Contract(pure = true)
    public StringDataRepository(final @NotNull CodecRegistry codecRegistry) {
        this.codecRegistry = Objects.requireNonNull(codecRegistry, "Codec registry cannot be null");
    }

    /**
     * Registers a data container with the given key.
     *
     * @param key      The key to associate with the data container.
     * @param container The data container to register.
     */
    public void register(final @NotNull String key, final @NotNull DataContainer container) {
        this.containers.put(key, container);
    }

    /**
     * Retrieves the data container associated with the given key.
     *
     * @param key The key whose associated data container is to be returned.
     * @return The data container associated with the specified key, or {@code null} if no container is found.
     */
    @Override
    public DataContainer getContainer(final @NotNull String key) {
        return this.containers.get(key);
    }

    /**
     * Reads and returns the key from the given buffer.
     *
     * @param buffer The buffer from which to read the key.
     * @return The key, or {@code null} if the key cannot be decoded.
     */
    @Override
    public @Nullable String readKey(final @NotNull ByteBuffer buffer) {
        return this.codecRegistry.decode(buffer, String.class);
    }

    /**
     * Gets the class type of the key used in this repository.
     *
     * @return The class type of the key.
     */
    @Override
    public @NotNull Class<String> getKeyClass() {
        return String.class;
    }

    /**
     * Gets the identifier for the key type.
     *
     * @return The identifier for the key type.
     */
    @Override
    public int getKeyIdentifier() {
        return 0;
    }

}