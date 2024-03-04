package be.darkkraft.memorized.server;

import be.darkkraft.memorized.codec.registry.CodecRegistry;
import be.darkkraft.memorized.server.auth.Authenticator;
import be.darkkraft.memorized.server.data.DataRepositoryCoordinator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * Builder for creating instances of {@link MemorizedServer}.
 */
public final class MemorizedServerBuilder {

    private InetSocketAddress address;
    private Authenticator authenticator;
    private CodecRegistry codecRegistry;
    private DataRepositoryCoordinator dataRepositoryCoordinator;
    private int workerThreads = 3;

    /**
     * Gets the address for the server.
     *
     * @return the address of the server.
     */
    @Nullable
    @Contract(pure = true)
    public InetSocketAddress address() {
        return this.address;
    }

    /**
     * Sets the address for the server.
     *
     * @param address the address for the server. Cannot be null.
     *
     * @return this builder instance.
     */
    @NotNull
    @Contract("_ -> this")
    public MemorizedServerBuilder address(final @NotNull InetSocketAddress address) {
        this.address = Objects.requireNonNull(address, "Address cannot be null");
        return this;
    }

    /**
     * Gets the {@link Authenticator} for the server.
     *
     * @return the authenticator for the server.
     */
    @Contract(pure = true)
    public Authenticator authenticator() {
        return this.authenticator;
    }

    /**
     * Sets the {@link Authenticator} for the server.
     *
     * @param authenticator the authenticator for the server. Cannot be null.
     *
     * @return this builder instance.
     */
    @NotNull
    @Contract("_ -> this")
    public MemorizedServerBuilder authenticator(final @NotNull Authenticator authenticator) {
        this.authenticator = Objects.requireNonNull(authenticator, "Authenticator cannot be null");
        return this;
    }

    /**
     * Gets the {@link CodecRegistry} for the server.
     *
     * @return the codec registry for the server.
     */
    @Contract(pure = true)
    public CodecRegistry codecRegistry() {
        return this.codecRegistry;
    }

    /**
     * Sets the codec registry for the server.
     *
     * @param codecRegistry the codec registry for the server. Cannot be null.
     *
     * @return this builder instance.
     */
    @NotNull
    @Contract("_ -> this")
    public MemorizedServerBuilder codecRegistry(final @NotNull CodecRegistry codecRegistry) {
        this.codecRegistry = Objects.requireNonNull(codecRegistry, "Codec registry cannot be null");
        return this;
    }

    /**
     * Gets the {@link DataRepositoryCoordinator} for the server.
     *
     * @return the DataRepositoryCoordinator for the server.
     */
    @Contract(pure = true)
    public DataRepositoryCoordinator dataRepositoryCoordinator() {
        return this.dataRepositoryCoordinator;
    }

    /**
     * Sets the {@link DataRepositoryCoordinator} for the server.
     *
     * @param dataRepositoryCoordinator the DataRepositoryCoordinator for the server. Cannot be null.
     *
     * @return this builder instance.
     */
    @NotNull
    @Contract("_ -> this")
    public MemorizedServerBuilder dataRepositoryCoordinator(final @NotNull DataRepositoryCoordinator dataRepositoryCoordinator) {
        this.dataRepositoryCoordinator = Objects.requireNonNull(dataRepositoryCoordinator, "DataRepository coordinator cannot be null");
        return this;
    }

    /**
     * Gets the number of worker threads for the server.
     *
     * @return the number of worker threads.
     */
    @Contract(pure = true)
    public int workerThreads() {
        return this.workerThreads;
    }

    /**
     * Sets the number of worker threads for the server.
     *
     * @param workerThreads the number of worker threads.
     *
     * @return this builder instance.
     */
    @NotNull
    @Contract("_ -> this")
    public MemorizedServerBuilder workerThreads(final int workerThreads) {
        this.workerThreads = workerThreads;
        return this;
    }

    /**
     * Builds and returns a new instance of {@link MemorizedServer}.
     *
     * @return a new instance of {@link MemorizedServer}.
     */
    @NotNull
    @Contract(" -> new")
    public MemorizedServer build() {
        return new MemorizedServerImpl(this.address,
                this.authenticator,
                this.codecRegistry,
                this.dataRepositoryCoordinator,
                this.workerThreads);
    }

}