package eu.psandro.tsjames.controller.event;

import eu.psandro.tsjames.io.event.RespondableNetPacketEvent;
import eu.psandro.tsjames.io.packet.PacketPing;
import lombok.NoArgsConstructor;

/**
 * @author PSandro on 31.08.18
 * @project tsjames
 */
@NoArgsConstructor
public class PingEvent extends RespondableNetPacketEvent<PacketPing> {

}
