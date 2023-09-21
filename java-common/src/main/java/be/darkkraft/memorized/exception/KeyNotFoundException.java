package be.darkkraft.memorized.exception;

/**
 * Custom exception class to handle situations where a key is not found.
 *
 * @see RuntimeException
 */
public class KeyNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@link KeyNotFoundException} with the specified key.
     *
     * @param key the key that was not found
     */
    public KeyNotFoundException(final Object key) {
        super(key + " key not found");
    }

}