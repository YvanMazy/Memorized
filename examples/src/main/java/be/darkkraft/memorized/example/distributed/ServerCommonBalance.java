package be.darkkraft.memorized.example.distributed;

import be.darkkraft.memorized.net.session.Session;
import be.darkkraft.memorized.packet.ServerPacket;
import be.darkkraft.memorized.server.data.container.DataContainer;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.ByteBuffer;

public class ServerCommonBalance implements DataContainer {

    private static final VarHandle BALANCE_HANDLE;

    static {
        try {
            BALANCE_HANDLE = MethodHandles.lookup().findVarHandle(ServerCommonBalance.class, "balance", double.class);
        } catch (final NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private volatile double balance;

    @Override
    public void handleUpdate(final @NotNull Session session, final @NotNull ByteBuffer buffer) {
        final boolean give = buffer.get() == 0;
        final double amount = buffer.getDouble();
        double current, target;
        byte result = 0;

        do {
            current = (double) BALANCE_HANDLE.getVolatile(this);
            target = give ? (current + amount) : (current - amount);
            if (target < 0) {
                result = 1;
                break;
            }
        } while (!BALANCE_HANDLE.compareAndSet(this, current, target));

        if (!give) {
            session.unsafeSend(ByteBuffer.allocate(2).put(ServerPacket.RESULT.getId()).put(result));
        }
    }

    @Override
    public void handleShow(final @NotNull Session session, final @NotNull ByteBuffer buffer) {
        session.unsafeSend(ByteBuffer.allocate(9).put(ServerPacket.RESULT.getId()).putDouble(this.balance));
    }

}