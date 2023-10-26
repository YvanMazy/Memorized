package be.darkkraft.memorized.example.simple.codec;

import be.darkkraft.memorized.codec.Codec;
import be.darkkraft.memorized.codec.registry.CodecRegistry;
import be.darkkraft.memorized.example.simple.Product;
import be.darkkraft.memorized.packet.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

import static be.darkkraft.memorized.codec.Codec.getString;

public final class ProductCodec implements Codec<Product> {

    @Override
    public void encode(final @NotNull CodecRegistry registry, final @NotNull ByteBuf buffer, final @NotNull Product value) {
        buffer.putString(value.getId()).putDouble(value.getPrice()).putInt(value.getQuantity());
    }

    @Override
    public Product decode(final @NotNull CodecRegistry registry, final @NotNull ByteBuffer buffer) {
        return new Product(getString(buffer), buffer.getDouble(), buffer.getInt());
    }

}