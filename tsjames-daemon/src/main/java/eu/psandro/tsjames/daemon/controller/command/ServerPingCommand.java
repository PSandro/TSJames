package eu.psandro.tsjames.daemon.controller.command;

import eu.psandro.tsjames.controller.console.command.Command;
import eu.psandro.tsjames.daemon.io.NetServerImpl;
import eu.psandro.tsjames.io.auth.NetSubject;
import eu.psandro.tsjames.io.packet.PacketPing;
import eu.psandro.tsjames.io.protocol.NetPacket;
import eu.psandro.tsjames.io.protocol.ResponseCall;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


/**
 * @author PSandro on 31.08.18
 * @project tsjames
 */
@RequiredArgsConstructor
public final class ServerPingCommand extends Command {

    @NonNull
    private final NetServerImpl server;

    @Override
    public String handleCommand(String[] args) {
        if (args.length == 1) {
            int id;
            try {
                id = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                return "ID is not a valid integer!";
            }

            final PacketPing packetPing = new PacketPing();
            packetPing.setResponseCall(netPacket -> System.out.println("Pong!"));
            this.server.sendPacket(NetSubject.byId(id), packetPing);
            return "ping request sent!";

        }
        return "ping <NetSubjectId>";
    }

    @Override
    public String getShortDescription() {
        return "Ping a NetSubject";
    }
}
