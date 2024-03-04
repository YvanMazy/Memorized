package be.darkkraft.memorized.data.key;

import be.darkkraft.memorized.exception.KeyNotFoundException;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Basic implementation of the {@link KeyRegistry} interface.
 *
 * @param <K> The type of keys to be registered.
 */
public class BasicKeyRegistry<K> implements KeyRegistry<K> {

    private final Int2ObjectMap<K> idToKey = new Int2ObjectOpenHashMap<>();
    private final Object2IntMap<K> keyToId = new Object2IntOpenHashMap<>();

    /**
     * Constructs a new {@link BasicKeyRegistry} instance.
     */
    public BasicKeyRegistry() {
        this.keyToId.defaultReturnValue(-1);
    }

    /**
     * Registers a new key with the specified identifier.
     *
     * @param key        The key to be registered.
     * @param identifier The identifier for the key.
     *
     * @return The {@link BasicKeyRegistry} instance for chaining.
     *
     * @throws IllegalArgumentException if the identifier is negative.
     */
    @Override
    @Contract("_, _ -> this")
    public @NotNull KeyRegistry<K> register(final @NotNull K key, final int identifier) {
        if (identifier < 0) {
            throw new IllegalArgumentException("Identifier must be positive");
        }
        this.idToKey.put(identifier, Objects.requireNonNull(key));
        this.keyToId.put(key, identifier);
        return this;
    }

    /**
     * Retrieves the class associated with the given identifier.
     *
     * @param identifier The identifier of the class to retrieve.
     *
     * @return The class associated with the identifier.
     */
    @Override
    public K getClassFromIdentifier(final int identifier) {
        return this.idToKey.get(identifier);
    }

    /**
     * Retrieves the identifier associated with the given class.
     *
     * @param key The class whose identifier is to be retrieved.
     *
     * @return The identifier associated with the class.
     *
     * @throws KeyNotFoundException if the key is not found in the registry.
     */
    @Override
    public int getIdentifierFromClass(final @NotNull K key) {
        final int i = this.keyToId.getInt(key);
        if (i < 0) {
            throw new KeyNotFoundException(key);
        }
        return i;
    }

}