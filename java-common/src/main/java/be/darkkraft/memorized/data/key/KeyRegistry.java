package be.darkkraft.memorized.data.key;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for a registry that holds mappings between keys and their corresponding identifiers.
 *
 * @param <K> the type of key this registry handles.
 */
public interface KeyRegistry<K> {

    /**
     * Registers a key with its corresponding identifier.
     *
     * @param key        the key to register
     * @param identifier the identifier for the key
     *
     * @return this {@link KeyRegistry}
     */
    @Contract("_, _ -> this")
    @NotNull KeyRegistry<K> register(final @NotNull K key, final int identifier);

    /**
     * Retrieves the key corresponding to an identifier.
     *
     * @param identifier the identifier for the key
     *
     * @return the key corresponding to the identifier, or {@code null} if not found
     */
    @Nullable K getClassFromIdentifier(final int identifier);

    /**
     * Retrieves the identifier corresponding to a key.
     *
     * @param key the key for which to get the identifier
     *
     * @return the identifier corresponding to the key
     */
    int getIdentifierFromClass(final @NotNull K key);

}