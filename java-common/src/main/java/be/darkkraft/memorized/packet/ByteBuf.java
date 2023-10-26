package be.darkkraft.memorized.packet;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class ByteBuf {

    private ByteBuffer buffer;
    private static final int DEFAULT_CAPACITY = 256;

    public ByteBuf() {
        this(DEFAULT_CAPACITY);
    }

    public ByteBuf(final int initialCapacity) {
        this.buffer = ByteBuffer.allocate(initialCapacity);
    }

    /**
     * Ensure capacity of the current {@link ByteBuffer}
     *
     * @param requiredCapacity the required capacity
     */
    public void ensureCapacity(final int requiredCapacity) {
        if (this.capacity() < requiredCapacity) {
            int newSize = this.capacity();
            while (newSize < requiredCapacity) {
                newSize *= 2;
            }

            final ByteBuffer newBuffer = ByteBuffer.allocate(newSize).order(this.buffer.order());
            this.buffer.flip();
            newBuffer.put(this.buffer);
            this.buffer = newBuffer;
        }
    }

    /**
     * Writes a byte
     *
     * @param b the byte to write
     *
     * @return the modified {@link ByteBuf}
     */
    public @NotNull ByteBuf put(final byte b) {
        this.ensureCapacity(this.position() + 1);
        this.buffer.put(b);
        return this;
    }

    /**
     * Writes a boolean
     *
     * @param b the boolean to write
     *
     * @return the modified {@link ByteBuf}
     */
    public @NotNull ByteBuf putBoolean(final boolean b) {
        return this.put((byte) (b ? 1 : 0));
    }

    /**
     * Writes an int
     *
     * @param i the int to write
     *
     * @return the modified {@link ByteBuf}
     */
    public ByteBuf putInt(final int i) {
        this.ensureCapacity(this.position() + 4);
        this.buffer.putInt(i);
        return this;
    }

    /**
     * Writes a long
     *
     * @param l the long to write
     *
     * @return the modified {@link ByteBuf}
     */
    public ByteBuf putLong(final long l) {
        this.ensureCapacity(this.position() + 8);
        this.buffer.putLong(l);
        return this;
    }

    /**
     * Writes a short
     *
     * @param s the short to write
     *
     * @return the modified {@link ByteBuf}
     */
    public ByteBuf putShort(final short s) {
        this.ensureCapacity(this.position() + 2);
        this.buffer.putShort(s);
        return this;
    }

    /**
     * Writes a double
     *
     * @param d the double to write
     *
     * @return the modified {@link ByteBuf}
     */
    public ByteBuf putDouble(final double d) {
        this.ensureCapacity(this.position() + 8);
        this.buffer.putDouble(d);
        return this;
    }

    /**
     * Writes a float
     *
     * @param f the float to write
     *
     * @return the modified {@link ByteBuf}
     */
    public ByteBuf putInt(final float f) {
        this.ensureCapacity(this.position() + 4);
        this.buffer.putFloat(f);
        return this;
    }

    /**
     * Writes a char
     *
     * @param c the char to write
     *
     * @return the modified {@link ByteBuf}
     */
    public ByteBuf putChar(final char c) {
        this.ensureCapacity(this.position() + 2);
        this.buffer.putChar(c);
        return this;
    }

    /**
     * Writes a byte array
     *
     * @param src the byte array to write
     *
     * @return the modified {@link ByteBuf}
     */
    public @NotNull ByteBuf put(final byte[] src) {
        this.ensureCapacity(this.position() + src.length);
        this.buffer.put(src);
        return this;
    }

    /**
     * Writes a {@link ByteBuffer}
     *
     * @param src the {@link ByteBuffer} to write
     *
     * @return the modified {@link ByteBuf}
     */
    public @NotNull ByteBuf put(final ByteBuffer src) {
        this.ensureCapacity(this.position() + src.remaining());
        this.buffer.put(src);
        return this;
    }

    /**
     * Writes a {@link String}
     *
     * @param string the {@link String} to write
     *
     * @return the modified {@link ByteBuf}
     */
    public @NotNull ByteBuf putString(final @NotNull String string) {
        final byte[] bytes = string.getBytes();
        this.ensureCapacity(this.position() + 4 + bytes.length);
        this.buffer.putInt(bytes.length).put(bytes);
        return this;
    }

    /**
     * Reads a {@link String} from a {@link ByteBuffer}.
     *
     * @param buffer the source {@link ByteBuffer}
     *
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
     * @return the underlying buffer capacity
     */
    public int capacity() {
        return this.buffer.capacity();
    }

    /**
     * @return the underlying buffer position
     */
    public int position() {
        return this.buffer.position();
    }

    /**
     * @return the underlying buffer limit
     */
    public int limit() {
        return this.buffer.limit();
    }

    /**
     * @return the underlying buffer
     */
    public ByteBuffer getBuffer() {
        return this.buffer;
    }

}