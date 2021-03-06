package eu.psandro.tsjames.io.protocol;

import eu.psandro.tsjames.api.exception.JamesIOReadException;
import eu.psandro.tsjames.io.auth.NetSubject;
import io.netty.buffer.ByteBuf;
import lombok.*;

import java.nio.charset.Charset;

/**
 * @author PSandro on 25.08.18
 * @project tsjames
 */
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public abstract class NetPacket {

    public static final byte MAGIC_NUMBER = 'P';

    public static final Charset CHARSET = Charset.forName("UTF-8");

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PROTECTED)
    private short packetId;

    @Setter(AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    private NetSubject sender; //sender is confirmed in encoder

    protected NetPacket(@NonNull Short packetId) {
        this.packetId = packetId;
    }

    protected ByteBuf deepWrite() {
        return this.write();
    }

    protected void deepRead(ByteBuf data) {
        this.read(data);
    }

    protected abstract ByteBuf write();

    protected abstract void read(ByteBuf data) throws JamesIOReadException;


}
