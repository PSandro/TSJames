package eu.psandro.tsjames.io.protocol;

import eu.psandro.tsjames.api.exception.JamesIOReadException;
import io.netty.buffer.ByteBuf;
import lombok.*;

@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class RespondableNetPacket extends NetPacket {

    @Setter
    @Getter
    private Integer respondId;

    @Override
    ByteBuf deepWrite() {
        return super.deepWrite().writeInt(this.respondId);
    }

    @Override
    void deepRead(ByteBuf data) {
        super.deepRead(data);
        if (data.readableBytes() < 4) throw new JamesIOReadException("Insufficient bytes!");
        this.respondId = data.readInt();
    }
}
