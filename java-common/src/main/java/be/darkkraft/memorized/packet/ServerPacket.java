package be.darkkraft.memorized.packet;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * Enum representing different server commands.
 */
public enum ServerPacket {

    NOT_AUTHENTICATED,
    AUTH_FAILED,
    AUTH_SUCCESS,
    DISCONNECT,
    RESULT,
    NOT_FOUND;

    private static final ServerPacket[] VALUES = values();
    private final byte id = (byte) (-128 + this.ordinal());

    /**
     * Returns a {@link ServerPacket} based on the provided ID.
     *
     * @param id The ID to look up.
     * @return The corresponding {@link ServerPacket}, or {@code null} if not found.
     */
    @Contract(pure = true)
    @Nullable
    public static ServerPacket fromId(final byte id) {
        for (final ServerPacket value : VALUES) {
            if (value.id == id) {
                return value;
            }
        }
        return null;
    }

    /**
     * Gets the ID associated with the server command.
     *
     * @return The ID of the server command.
     */
    @Contract(pure = true)
    public byte getId() {
        return this.id;
    }

}