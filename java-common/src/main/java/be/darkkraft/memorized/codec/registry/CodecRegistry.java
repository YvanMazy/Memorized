package be.darkkraft.memorized.codec.registry;

import be.darkkraft.memorized.codec.Codec;
import be.darkkraft.memorized.packet.ByteBuf;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * Interface for a registry that holds mappings between types and their corresponding {@link Codec} instances.
 *
 * @see Codec
 * @see Class
 * @see ByteBuf
 */
public interface CodecRegistry {

    /**
     * Registers a {@link Codec} for a given type.
     *
     * @param typeClass the class of the type
     * @param codec     the {@link Codec} to use for the type
     * @param <T>       the type to register
     *
     * @return the updated CodecRegistry
     */
    @Contract("_, _ -> this")
    @NotNull <T> CodecRegistry register(final @NotNull Class<T> typeClass, final @NotNull Codec<T> codec);

    /**
     * Encodes an object to a {@link ByteBuf}.
     *
     * @param buffer the target {@link ByteBuf}
     * @param data   the object to encode
     *
     * @return the {@link ByteBuf} containing the encoded data
     */
    @Contract("_, _ -> param1")
    @NotNull ByteBuf encode(final @NotNull ByteBuf buffer, final @Nullable Object data);

    /**
     * Decodes an object from a {@link ByteBuffer}.
     *
     * @param buffer    the source {@link ByteBuffer}
     * @param typeClass the class of the type to decode
     * @param <T>       the type to decode
     *
     * @return the decoded object
     */
    @Nullable <T> T decode(final @NotNull ByteBuffer buffer, final @NotNull Class<T> typeClass);

    /**
     * Get a {@link Codec} from type class.
     *
     * @param typeClass the class of type
     *
     * @return the {@link Codec} of type class or null if not found
     */
    @Nullable <T> Codec<T> getCodec(final @NotNull Class<T> typeClass);

}