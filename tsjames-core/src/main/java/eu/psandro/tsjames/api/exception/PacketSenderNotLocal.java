package eu.psandro.tsjames.api.exception;

public class PacketSenderNotLocal extends JamesPacketException {
    public PacketSenderNotLocal(Integer tried, Integer actual) {
        super("Tried to send a packet with id " + tried + "; actual local id: " + actual);
    }
}
