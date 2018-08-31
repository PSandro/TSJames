package eu.psandro.tsjames.controller.listener;

import eu.psandro.tsjames.controller.event.PingEvent;
import eu.psandro.tsjames.io.event.NetEvent;
import eu.psandro.tsjames.io.event.NetEventListener;
import eu.psandro.tsjames.io.packet.PacketPing;

/**
 * @author PSandro on 31.08.18
 * @project tsjames
 */
public final class PingListener implements NetEventListener {

    @NetEvent
    public void onPing(PingEvent event) {
        final PacketPing response = new PacketPing();
        response.setRespondId(event.getResponseId());
        System.out.println("Got pinged!");
        event.respond(response);
    }
}
