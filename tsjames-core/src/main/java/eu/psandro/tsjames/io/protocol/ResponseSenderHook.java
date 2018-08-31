package eu.psandro.tsjames.io.protocol;

import eu.psandro.tsjames.io.auth.NetSubject;


/**
 * @author PSandro on 31.08.18
 * @project tsjames
 */
@FunctionalInterface
public interface ResponseSenderHook {

    void sendResponse(NetPacket response, NetSubject target);
}
