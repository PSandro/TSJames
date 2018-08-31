package eu.psandro.tsjames.io.protocol;

import eu.psandro.tsjames.api.exception.JamesIOReadException;
import eu.psandro.tsjames.io.auth.NetSubject;
import io.netty.buffer.ByteBuf;
import lombok.*;

@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class RespondableNetPacket extends NetPacket {

    @Setter
    @Getter
    private Integer respondId;

    @Setter
    @Getter
    private NetSubject responseTarget;

    @Override
    ByteBuf deepWrite() {
        return super.deepWrite()
                .writeInt(this.respondId)
                .writeInt(this.responseTarget.getId());
    }

    @Override
    void deepRead(ByteBuf data) {
        super.deepRead(data);
        if (data.readableBytes() < 8) throw new JamesIOReadException("Insufficient bytes!");
        this.respondId = data.readInt();
        this.responseTarget = NetSubject.byId(data.readInt());
    }
}
