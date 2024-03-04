package be.darkkraft.memorized.codec.base;

import be.darkkraft.memorized.codec.Codec;
import be.darkkraft.memorized.codec.registry.CodecRegistry;
import be.darkkraft.memorized.packet.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * Implementation of {@link Codec} for encoding and decoding {@link Long} objects.
 *
 * @see Codec
 * @see CodecRegistry
 * @see ByteBuffer
 */
public class LongCodec implements Codec<Long> {

    /**
     * Encodes a {@link Long} value and writes it to a {@link ByteBuf}.
     *
     * @param registry the {@link CodecRegistry} containing available codecs
     * @param buffer   the target {@link ByteBuf}
     * @param value    the {@link Long} value to encode
     */
    @Override
    public void encode(final @NotNull CodecRegistry registry, final @NotNull ByteBuf buffer, final @NotNull Long value) {
        buffer.putLong(value);
    }

    /**
     * Decodes a {@link Long} value from a {@link ByteBuffer}.
     *
     * @param registry the {@link CodecRegistry} containing available codecs
     * @param buffer   the source {@link ByteBuffer}
     *
     * @return the decoded {@link Long} value
     */
    @Override
    public Long decode(final @NotNull CodecRegistry registry, final @NotNull ByteBuffer buffer) {
        return buffer.getLong();
    }

}