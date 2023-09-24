package be.darkkraft.memorized.example.distributed;

import be.darkkraft.memorized.client.MemorizedClient;
import be.darkkraft.memorized.client.data.IdentifiableClientAccessor;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;

import static be.darkkraft.memorized.packet.ClientPacket.SHOW;
import static be.darkkraft.memorized.packet.ClientPacket.UPDATE;

public class ClientCommonBalance extends IdentifiableClientAccessor implements CommonBalance {

    private final String id;

    public ClientCommonBalance(final MemorizedClient client, final String id) {
        super(client);
        this.id = id;
    }

    @Override
    public String id() {
        return this.id;
    }

    @Override
    public CompletableFuture<Double> asyncBalance() {
        return this.queue(this.writeId(ByteBuffer.allocate(256).put(SHOW.getId())))
                .thenApply(buffer -> buffer == null ? 0 : buffer.getDouble());
    }

    @Override
    public void give(final double amount) {
        this.write(this.writeId(ByteBuffer.allocate(256).put(UPDATE.getId())).put((byte) 0).putDouble(amount));
    }

    @Override
    public CompletableFuture<Boolean> asyncTryToTake(final double amount) {
        return this.queue(this.writeId(ByteBuffer.allocate(256).put(UPDATE.getId())).put((byte) 1).putDouble(amount))
                .thenApply(buffer -> buffer != null && buffer.get() == 0);
    }

    @Override
    protected @NotNull Class<?> getKeyClass() {
        return String.class;
    }

    @Override
    protected @NotNull String getKeyId() {
        return this.id;
    }

}