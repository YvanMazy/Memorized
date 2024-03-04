package be.darkkraft.memorized.data.counter;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * Enum representing different types of counter updates.
 */
public enum CounterUpdate {

    SET,
    RESET,
    GET_AND_SET,
    INCREMENT_AND_GET,
    GET_AND_INCREMENT,
    DECREMENT_AND_GET,
    GET_AND_DECREMENT;

    private static final CounterUpdate[] VALUES = values();
    private final byte id = (byte) (-128 + this.ordinal());

    /**
     * Converts a byte ID to its corresponding {@link CounterUpdate} enum value.
     *
     * @param id the byte ID
     *
     * @return the corresponding {@link CounterUpdate} value or {@code null} if no match is found
     */
    @Contract(pure = true)
    @Nullable
    public static CounterUpdate fromId(final byte id) {
        for (final CounterUpdate value : VALUES) {
            if (value.id == id) {
                return value;
            }
        }
        return null;
    }

    /**
     * Gets the byte ID of the {@link CounterUpdate} enum value.
     *
     * @return the byte ID
     */
    @Contract(pure = true)
    public byte getId() {
        return this.id;
    }

}