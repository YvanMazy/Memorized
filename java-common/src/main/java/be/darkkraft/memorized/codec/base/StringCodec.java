package be.darkkraft.memorized.codec.base;

import be.darkkraft.memorized.codec.Codec;
import be.darkkraft.memorized.codec.registry.CodecRegistry;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

import static be.darkkraft.memorized.codec.Codec.getString;
import static be.darkkraft.memorized.codec.Codec.putString;

/**
 * Implementation of {@link Codec} for encoding and decoding {@link String} objects.
 *
 * @see Codec
 * @see CodecRegistry
 * @see ByteBuffer
 */
public class StringCodec implements Codec<String> {

    /**
     * Encodes a {@link String} value and writes it to a {@link ByteBuffer}.
     *
     * @param registry the {@link CodecRegistry} containing available codecs
     * @param buffer   the target {@link ByteBuffer}
     * @param value    the {@link String} value to encode
     */
    @Override
    public void encode(final @NotNull CodecRegistry registry, final @NotNull ByteBuffer buffer, final @NotNull String value) {
        putString(buffer, value);
    }

    /**
     * Decodes a {@link String} value from a {@link ByteBuffer}.
     *
     * @param registry the {@link CodecRegistry} containing available codecs
     * @param buffer   the source {@link ByteBuffer}
     * @return the decoded {@link String} value
     */
    @Override
    public String decode(final @NotNull CodecRegistry registry, final @NotNull ByteBuffer buffer) {
        return getString(buffer);
    }

}