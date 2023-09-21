package be.darkkraft.memorized.codec.registry;

import be.darkkraft.memorized.codec.Codec;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * Interface for a registry that holds mappings between types and their corresponding {@link Codec} instances.
 *
 * @see Codec
 * @see Class
 * @see ByteBuffer
 */
public interface CodecRegistry {

    /**
     * Registers a {@link Codec} for a given type.
     *
     * @param typeClass the class of the type
     * @param codec     the {@link Codec} to use for the type
     * @param <T>       the type to register
     * @return the updated CodecRegistry
     */
    @Contract("_, _ -> this")
    @NotNull <T> CodecRegistry register(final @NotNull Class<T> typeClass, final @NotNull Codec<T> codec);

    /**
     * Encodes an object to a {@link ByteBuffer}.
     *
     * @param buffer the target {@link ByteBuffer}
     * @param data   the object to encode
     * @return the {@link ByteBuffer} containing the encoded data
     */
    @Contract("_, _ -> param1")
    @NotNull ByteBuffer encode(final @NotNull ByteBuffer buffer, final @Nullable Object data);

    /**
     * Decodes an object from a {@link ByteBuffer}.
     *
     * @param buffer    the source {@link ByteBuffer}
     * @param typeClass the class of the type to decode
     * @param <T>       the type to decode
     * @return the decoded object
     */
    @Nullable <T> T decode(final @NotNull ByteBuffer buffer, final @NotNull Class<T> typeClass);

}