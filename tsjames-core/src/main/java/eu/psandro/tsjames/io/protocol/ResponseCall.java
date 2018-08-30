package eu.psandro.tsjames.io.protocol;

import com.sun.istack.internal.Nullable;

@FunctionalInterface
public interface ResponseCall {

    void onCall(final @Nullable NetPacket netPacket);

}
