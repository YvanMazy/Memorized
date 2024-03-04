package be.darkkraft.memorized.client;

import be.darkkraft.memorized.client.auth.AuthenticationInput;
import be.darkkraft.memorized.client.config.ClientConfiguration;
import be.darkkraft.memorized.codec.registry.CodecRegistry;
import be.darkkraft.memorized.data.key.KeyRegistry;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * Builder class responsible for creating a {@link MemorizedClient} instance.
 */
public final class MemorizedClientBuilder {

    private InetSocketAddress serverAddress;
    private AuthenticationInput authenticationInput;
    private CodecRegistry codecRegistry;
    private KeyRegistry<Class<?>> keyRegistry;
    private ClientConfiguration configuration;

    /**
     * Retrieves the server address to be used by the client.
     *
     * @return The {@link InetSocketAddress} representing the server address.
     */
    @Nullable
    @Contract(pure = true)
    public InetSocketAddress serverAddress() {
        return this.serverAddress;
    }

    /**
     * Sets the server address for the client.
     *
     * @param serverAddress The {@link InetSocketAddress} representing the server address. Cannot be null.
     *
     * @return This {@link MemorizedClientBuilder} instance.
     */
    @NotNull
    @Contract("_ -> this")
    public MemorizedClientBuilder serverAddress(final @NotNull InetSocketAddress serverAddress) {
        this.serverAddress = Objects.requireNonNull(serverAddress, "Server address cannot be null");
        return this;
    }

    /**
     * Retrieves the authentication input.
     *
     * @return The {@link AuthenticationInput} to be used for authentication.
     */
    @Nullable
    @Contract(pure = true)
    public AuthenticationInput authenticationInput() {
        return this.authenticationInput;
    }

    /**
     * Sets the authentication input for the client.
     *
     * @param authenticationInput The {@link AuthenticationInput} to set for authentication. Cannot be null.
     *
     * @return This {@link MemorizedClientBuilder} instance.
     */
    @NotNull
    @Contract("_ -> this")
    public MemorizedClientBuilder authenticationInput(final @NotNull AuthenticationInput authenticationInput) {
        this.authenticationInput = Objects.requireNonNull(authenticationInput, "Authentication input cannot be null");
        return this;
    }

    /**
     * Retrieves the {@link CodecRegistry} for the client.
     *
     * @return The {@link CodecRegistry}.
     */
    @Nullable
    @Contract(pure = true)
    public CodecRegistry codecRegistry() {
        return this.codecRegistry;
    }

    /**
     * Sets the {@link CodecRegistry} for the client.
     *
     * @param codecRegistry The {@link CodecRegistry} to set for the client. Cannot be null.
     *
     * @return This {@link MemorizedClientBuilder} instance.
     */
    @NotNull
    @Contract("_ -> this")
    public MemorizedClientBuilder codecRegistry(final @NotNull CodecRegistry codecRegistry) {
        this.codecRegistry = Objects.requireNonNull(codecRegistry, "Codec registry cannot be null");
        return this;
    }

    /**
     * Retrieves the {@link KeyRegistry} for the client.
     *
     * @return The {@link KeyRegistry}.
     */
    @Nullable
    @Contract(pure = true)
    public KeyRegistry<Class<?>> keyRegistry() {
        return this.keyRegistry;
    }

    /**
     * Sets the {@link KeyRegistry} for the client.
     *
     * @param keyRegistry The {@link KeyRegistry} to set for the client. Cannot be null.
     *
     * @return This {@link MemorizedClientBuilder} instance.
     */
    @NotNull
    @Contract("_ -> this")
    public MemorizedClientBuilder keyRegistry(final @NotNull KeyRegistry<Class<?>> keyRegistry) {
        this.keyRegistry = Objects.requireNonNull(keyRegistry, "Key registry cannot be null");
        return this;
    }

    /**
     * Retrieves the {@link ClientConfiguration} for the client.
     *
     * @return The {@link ClientConfiguration}.
     */
    @Nullable
    @Contract(pure = true)
    public ClientConfiguration configuration() {
        return this.configuration;
    }

    /**
     * Sets the {@link ClientConfiguration} for the client.
     *
     * @param configuration The {@link ClientConfiguration} to set for the client.
     *
     * @return This {@link MemorizedClientBuilder} instance.
     */
    @NotNull
    @Contract("_ -> this")
    public MemorizedClientBuilder configuration(final @NotNull ClientConfiguration configuration) {
        this.configuration = Objects.requireNonNull(configuration, "Configuration cannot be null");
        return this;
    }

    /**
     * Builds and returns a new {@link MemorizedClient} instance using the current configurations.
     *
     * @return A new {@link MemorizedClient} instance.
     */
    @NotNull
    @Contract(" -> new")
    public MemorizedClient build() {
        return new MemorizedClientImpl(this.serverAddress,
                this.authenticationInput,
                this.codecRegistry,
                this.keyRegistry,
                Objects.requireNonNullElseGet(this.configuration, () -> ClientConfiguration.builder().build()));
    }

}