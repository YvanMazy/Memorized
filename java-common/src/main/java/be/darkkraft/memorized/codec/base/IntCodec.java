package be.darkkraft.memorized.codec.base;

import be.darkkraft.memorized.codec.Codec;
import be.darkkraft.memorized.codec.registry.CodecRegistry;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * Implementation of {@link Codec} for encoding and decoding {@link Integer} objects.
 *
 * @see Codec
 * @see CodecRegistry
 * @see ByteBuffer
 * @see Integer
 */
public class IntCodec implements Codec<Integer> {

    /**
     * Encodes an {@link Integer} value and writes it to a {@link ByteBuffer}.
     *
     * @param registry the {@link CodecRegistry} containing available codecs
     * @param buffer   the target {@link ByteBuffer}
     * @param value    the {@link Integer} value to encode
     */
    @Override
    public void encode(final @NotNull CodecRegistry registry, final @NotNull ByteBuffer buffer, final @NotNull Integer value) {
        buffer.putInt(value);
    }

    /**
     * Decodes an {@link Integer} value from a {@link ByteBuffer}.
     *
     * @param registry the {@link CodecRegistry} containing available codecs
     * @param buffer   the source {@link ByteBuffer}
     * @return the decoded {@link Integer} value
     */
    @Override
    public Integer decode(final @NotNull CodecRegistry registry, final @NotNull ByteBuffer buffer) {
        return buffer.getInt();
    }

}