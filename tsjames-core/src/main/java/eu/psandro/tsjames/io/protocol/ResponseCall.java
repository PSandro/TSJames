package eu.psandro.tsjames.io.protocol;


@FunctionalInterface
public interface ResponseCall {

    void onCall(final  NetPacket netPacket);

}
