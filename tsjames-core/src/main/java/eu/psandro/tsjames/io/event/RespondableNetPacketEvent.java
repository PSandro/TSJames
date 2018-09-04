package eu.psandro.tsjames.io.event;

import eu.psandro.tsjames.io.auth.NetSubject;
import eu.psandro.tsjames.io.protocol.NetPacket;
import eu.psandro.tsjames.io.protocol.RespondableNetPacket;
import lombok.Getter;
import lombok.NonNull;

public abstract class RespondableNetPacketEvent<T extends RespondableNetPacket> extends NetPacketEvent<T> {

    @Getter
    private int responseId;

    @Getter
    private NetSubject respondTarget;

    private NetPacket response;


    public void respond(@NonNull T netPacket) {
        netPacket.setResponseTarget(this.respondTarget);
        this.response = netPacket;
    }



    protected NetPacket getResponse() {
        return this.response;
    }

    @Override
    public void read(T packet) {
        this.responseId = packet.getRespondId();
        this.respondTarget = packet.getResponseTarget();
    }
}
