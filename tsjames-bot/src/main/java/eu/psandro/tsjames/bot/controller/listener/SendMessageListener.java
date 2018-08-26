package eu.psandro.tsjames.bot.controller.listener;

import eu.psandro.tsjames.io.event.NetEvent;
import eu.psandro.tsjames.io.event.NetEventListener;
import eu.psandro.tsjames.io.packet.PacketSendMessage;

/**
 * @author PSandro on 26.08.18
 * @project tsjames
 */
public final class SendMessageListener implements NetEventListener {

    @NetEvent
    public void onMessage(PacketSendMessage packet) {
        final String message = packet.getMessage();
        final long userId = packet.getRecipientUserId();

        //TODO send message

    }
}
