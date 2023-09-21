package be.darkkraft.memorized.packet;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * Enum representing different client commands.
 */
public enum ClientPacket {

    AUTH,
    CREATE,
    SHOW,
    UPDATE,
    DELETE,
    DISCONNECT;

    private static final ClientPacket[] VALUES = values();
    private final byte id = (byte) (-128 + this.ordinal());

    /**
     * Returns a {@link ClientPacket} based on the provided ID.
     *
     * @param id The ID to look up.
     * @return The corresponding {@link ClientPacket}, or {@code null} if not found.
     */
    @Contract(pure = true)
    @Nullable
    public static ClientPacket fromId(final byte id) {
        for (final ClientPacket value : VALUES) {
            if (value.id == id) {
                return value;
            }
        }
        return null;
    }

    /**
     * Gets the ID associated with the client command.
     *
     * @return The ID of the client command.
     */
    @Contract(pure = true)
    public byte getId() {
        return this.id;
    }

}