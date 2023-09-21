package be.darkkraft.memorized.packet.handler.registry;

import be.darkkraft.memorized.net.session.Session;
import be.darkkraft.memorized.packet.handler.PacketHandler;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

/**
 * Abstract class for managing a registry of {@link PacketHandler PacketHandlers}.
 *
 * @param <E> The enum type that represents the different commands
 * @param <I> The type of the instance passed during initialization
 * @param <T> The type parameter of the packet handler
 *
 * @see PacketHandler
 * @see EnumMap
 */
public abstract class PacketHandlerRegistry<E extends Enum<E>, I, T extends Session> {

    private final Map<E, PacketHandler<T>> handlers;

    /**
     * Initializes the packet handler registry.
     *
     * @param enumClass The class of the enum that defines commands
     */
    protected PacketHandlerRegistry(final @NotNull Class<E> enumClass) {
        this.handlers = new EnumMap<>(enumClass);
    }

    /**
     * Initializes the packet handler registry using an instance.
     *
     * @param instance The instance used to initialize the registry
     */
    public abstract void initialize(final @NotNull I instance);

    /**
     * Retrieves a {@link PacketHandler} based on the command.
     *
     * @param command The command to search for
     * @return The packet handler for the command, or {@code null} if not found
     */
    @Contract(pure = true)
    @Nullable
    public PacketHandler<T> getHandler(final @NotNull E command) {
        return this.handlers.get(command);
    }

    /**
     * Registers a {@link PacketHandler} with a command.
     *
     * @param command The command to associate with the packet handler
     * @param handler The packet handler to register
     */
    protected void register(final @NotNull E command, final @NotNull PacketHandler<T> handler) {
        this.handlers.put(command, handler);
    }

    /**
     * Retrieves a map of all registered handlers.
     *
     * @return A map containing all registered handlers
     */
    @Contract(pure = true)
    @NotNull
    public Map<E, PacketHandler<T>> getHandlers() {
        return this.handlers;
    }

}