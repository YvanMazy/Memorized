package be.darkkraft.memorized.data.map;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * Enum representing different types of map updates.
 */
public enum MapUpdate {

    SET,
    REMOVE;

    private static final MapUpdate[] VALUES = values();
    private final byte id = (byte) (-128 + this.ordinal());

    /**
     * Converts a byte ID to its corresponding {@link MapUpdate} enum value.
     *
     * @param id the byte ID
     *
     * @return the corresponding {@link MapUpdate} value or {@code null} if no match is found
     */
    @Contract(pure = true)
    @Nullable
    public static MapUpdate fromId(final byte id) {
        for (final MapUpdate value : VALUES) {
            if (value.id == id) {
                return value;
            }
        }
        return null;
    }

    /**
     * Gets the byte ID of the {@link MapUpdate} enum value.
     *
     * @return the byte ID
     */
    @Contract(pure = true)
    public byte getId() {
        return this.id;
    }

}