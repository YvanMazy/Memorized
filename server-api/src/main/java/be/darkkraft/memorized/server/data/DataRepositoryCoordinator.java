package be.darkkraft.memorized.server.data;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Coordinator class for managing multiple {@link DataRepository DataRepositories}.
 *
 * @see DataRepository
 */
public final class DataRepositoryCoordinator {

    /**
     * Deliberately non-thread-safe for better performance, assuming that writes are done before the server starts.
     */
    private final Int2ObjectMap<DataRepository<?>> idToRepository = new Int2ObjectOpenHashMap<>();
    private final Map<Class<?>, DataRepository<?>> classToRepository = new HashMap<>();

    /**
     * Registers a {@link DataRepository} with the coordinator.
     *
     * @param repository The {@link DataRepository} to register.
     * @return This {@link DataRepositoryCoordinator} instance.
     */
    @Contract("_ -> this")
    @NotNull
    public DataRepositoryCoordinator register(final @NotNull DataRepository<?> repository) {
        this.idToRepository.put(Objects.requireNonNull(repository, "Repository cannot be null").getKeyIdentifier(), repository);
        this.classToRepository.put(repository.getKeyClass(), repository);
        return this;
    }

    /**
     * Retrieves a {@link DataRepository} based on its identifier.
     *
     * @param repositoryIdentifier The identifier of the repository.
     * @return The {@link DataRepository} corresponding to the identifier, or {@code null} if not found.
     */
    @Nullable
    @Contract(pure = true)
    public DataRepository<?> getRepository(final int repositoryIdentifier) {
        return this.idToRepository.get(repositoryIdentifier);
    }

    /**
     * Retrieves a {@link DataRepository} based on its class.
     *
     * @param clazz The {@link Class} object representing the type of key used in the repository.
     * @return The {@link DataRepository} corresponding to the class, or {@code null} if not found.
     */
    @Nullable
    @Contract(pure = true)
    public DataRepository<?> getRepository(final @NotNull Class<?> clazz) {
        return this.classToRepository.get(Objects.requireNonNull(clazz, "Repository class cannot be null"));
    }

    /**
     * Retrieves the key identifier for a given class.
     *
     * @param clazz The {@link Class} object representing the type of key used in the repository.
     * @return The key identifier.
     */
    @Contract(pure = true)
    public int getKeyIdentifier(final @NotNull Class<?> clazz) {
        return Objects.requireNonNull(this.getRepository(clazz), "Repository not found for class").getKeyIdentifier();
    }

}