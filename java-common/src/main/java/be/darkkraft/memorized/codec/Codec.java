package be.darkkraft.memorized.codec;

import be.darkkraft.memorized.codec.registry.CodecRegistry;
import be.darkkraft.memorized.packet.ByteBuf;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * Interface representing a codec for encoding and decoding objects.
 *
 * @param <T> the type of object this codec handles
 *
 * @see ByteBuffer
 * @see CodecRegistry
 */
public interface Codec<T> {

    /**
     * Writes a {@link String} to a {@link ByteBuffer}.
     *
     * @param buffer the target {@link ByteBuffer}
     * @param string the {@link String} to write
     * @return the modified {@link ByteBuffer}
     */
    @Contract("_, _ -> param1")
    @NotNull
    static ByteBuffer putString(final @NotNull ByteBuffer buffer, final @NotNull String string) {
        final byte[] bytes = string.getBytes();
        buffer.putInt(bytes.length).put(bytes);
        return buffer;
    }

    /**
     * Reads a {@link String} from a {@link ByteBuffer}.
     *
     * @param buffer the source {@link ByteBuffer}
     * @return the extracted {@link String}
     */
    @Contract("_ -> new")
    @NotNull
    static String getString(final @NotNull ByteBuffer buffer) {
        final int length = buffer.getInt();
        final byte[] bytes = new byte[length];
        buffer.get(bytes);
        return new String(bytes);
    }

    /**
     * Encodes an object and writes it to a {@link ByteBuf}.
     *
     * @param registry the {@link CodecRegistry} containing available codecs
     * @param buffer   the target ByteBuffer
     * @param value    the object to encode
     */
    void encode(final @NotNull CodecRegistry registry, final @NotNull ByteBuf buffer, final @NotNull T value);

    /**
     * Decodes an object from a {@link ByteBuffer}.
     *
     * @param registry the CodecRegistry containing available codecs
     * @param buffer   the source {@link ByteBuffer}
     * @return the decoded object
     */
    @Nullable T decode(final @NotNull CodecRegistry registry, final @NotNull ByteBuffer buffer);

}
