package be.darkkraft.memorized.codec.registry;

import be.darkkraft.memorized.codec.Codec;
import be.darkkraft.memorized.codec.base.IntCodec;
import be.darkkraft.memorized.codec.base.LongCodec;
import be.darkkraft.memorized.codec.base.StringCodec;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Default implementation of {@link CodecRegistry} for registering and using codecs.
 *
 * @see CodecRegistry
 * @see Codec
 * @see ByteBuffer
 */
public class DefaultCodecRegistry implements CodecRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCodecRegistry.class);

    /**
     * Map for holding registered codecs.
     * Deliberately non-thread-safe for better performance, assuming that writes are done before the server starts.
     */
    private final Map<Class<?>, Codec<?>> codecMap = new HashMap<>();

    /**
     * Registers default codecs.
     *
     * @return this {@link DefaultCodecRegistry}
     */
    @Contract(" -> this")
    public DefaultCodecRegistry registerDefaults() {
        this.register(String.class, new StringCodec())
                .register(Integer.class, new IntCodec())
                .register(int.class, new IntCodec())
                .register(Long.class, new LongCodec())
                .register(long.class, new LongCodec());
        return this;
    }

    /**
     * Registers a {@link Codec} for a given type.
     *
     * @param typeClass the class of the type
     * @param codec     the {@link Codec} to use for the type
     * @param <T>       the type to register
     * @return this {@link DefaultCodecRegistry}
     */
    @Contract("_, _ -> this")
    @Override
    public <T> @NotNull DefaultCodecRegistry register(final @NotNull Class<T> typeClass, final @NotNull Codec<T> codec) {
        this.codecMap.put(typeClass, codec);
        return this;
    }

    /**
     * Encodes an object to a {@link ByteBuffer}.
     *
     * @param buffer the target {@link ByteBuffer}
     * @param data   the object to encode
     * @return the {@link ByteBuffer} containing the encoded data
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Contract("_, _ -> param1")
    @Override
    public @NotNull ByteBuffer encode(final @NotNull ByteBuffer buffer, final Object data) {
        final Class<?> typeClass = Objects.requireNonNull(data, "Data cannot be null").getClass();
        final Codec codec = this.codecMap.get(typeClass);
        if (codec == null) {
            LOGGER.error("No codec found for {}", typeClass);
            return buffer;
        }
        codec.encode(this, buffer, data);
        return buffer;
    }

    /**
     * Decodes an object from a {@link ByteBuffer}.
     *
     * @param buffer    the source {@link ByteBuffer}
     * @param typeClass the class of the type to decode
     * @param <T>       the type to decode
     * @return the decoded object
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T decode(final @NotNull ByteBuffer buffer, final @NotNull Class<T> typeClass) {
        final Codec<T> codec = (Codec<T>) this.codecMap.get(Objects.requireNonNull(typeClass, "Type class cannot be null"));
        if (codec == null) {
            LOGGER.error("No codec found for {}", typeClass);
            return null;
        }
        return codec.decode(this, buffer);
    }

}