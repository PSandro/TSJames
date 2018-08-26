package eu.psandro.tsjames.io.event;

import eu.psandro.tsjames.io.protocol.NetPacket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author PSandro on 26.08.18
 * @project tsjames
 */
@AllArgsConstructor
@Getter
public class NetEventHandler implements Comparable<NetEventHandler> {

    private final NetEventListener listener;
    private final Method method;
    private final NetEvent annotation;


    public void execute(@NonNull NetPacket packetEvent) {
        try {
            method.invoke(this.listener, packetEvent);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int compareTo(NetEventHandler other) {
        int annotation = this.annotation.priority() - other.annotation.priority();
        if (annotation == 0)
            annotation = this.listener.hashCode() - other.listener.hashCode();
        return annotation == 0 ? this.hashCode() - other.hashCode() : annotation;
    }
}
