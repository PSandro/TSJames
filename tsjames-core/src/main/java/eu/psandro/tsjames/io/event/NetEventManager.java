package eu.psandro.tsjames.io.event;

import eu.psandro.tsjames.io.protocol.NetPacket;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author PSandro on 26.08.18
 * @project tsjames
 */
@NoArgsConstructor
public class NetEventManager implements Closeable {
    private static final NetEventHandler[] EMPTYHANDLERS = {};

    private final Map<Class<? extends NetPacket>, Collection<NetEventHandler>> bindings = new HashMap<>();
    private final Set<NetEventListener> registeredListeners = new HashSet<>();


    public NetEventHandler[] getListenersFor(Class<? extends NetPacket> clazz) {
        final Collection<NetEventHandler> handlers = this.bindings.get(clazz);
        if (handlers == null || handlers.isEmpty())
            return EMPTYHANDLERS;
        return handlers.toArray(new NetEventHandler[handlers.size()]);
    }

    public <T extends NetPacket> T executePacketEvent(T packetEvent) {
        final Collection<NetEventHandler> handlers = this.bindings.get(packetEvent.getClass());
        if (handlers == null) {
            System.out.println("no handlers found for packet " + packetEvent.getClass().getSimpleName());
            return packetEvent;
        }
        handlers.forEach(handler -> handler.execute(packetEvent));
        return packetEvent;
    }

    public void registerListener(final NetEventListener listener) {

        final Method[] methods = listener.getClass().getDeclaredMethods();
        this.registeredListeners.add(listener);
        for (final Method method : methods) {
            final NetEvent annotation = method.getAnnotation(NetEvent.class);
            if (annotation == null)
                continue;

            final Class<?>[] parameters = method.getParameterTypes();
            if (parameters.length != 1)
                continue;

            final Class<?> param = parameters[0];

            if (!method.getReturnType().equals(void.class)) {
                continue;
            }

            if (NetPacket.class.isAssignableFrom(param)) {
                final Class<? extends NetPacket> realParam = (Class<? extends NetPacket>) param;

                if (!this.bindings.containsKey(realParam)) {
                    this.bindings.put(realParam, new TreeSet<>());
                }
                final Collection<NetEventHandler> eventHandlersForEvent = this.bindings.get(realParam);
                final NetEventHandler eventHandler = createEventHandler(listener, method, annotation);
                eventHandlersForEvent.add(eventHandler);
            }
        }
    }

    private NetEventHandler createEventHandler(final NetEventListener listener, final Method method, final NetEvent annotation) {
        return new NetEventHandler(listener, method, annotation);
    }


    public void removeListener(NetEventListener listener) {
        for (Map.Entry<Class<? extends NetPacket>, Collection<NetEventHandler>> ee : bindings.entrySet()) {
            ee.getValue().removeIf(curr -> curr.getListener() == listener);
        }
        this.registeredListeners.remove(listener);
    }

    public Map<Class<? extends NetPacket>, Collection<NetEventHandler>> getBindings() {
        return new HashMap<>(this.bindings);
    }

    public Set<NetEventListener> getRegisteredListeners() {
        return new HashSet<>(this.registeredListeners);
    }

    @Override
    public void close() throws IOException {
        this.bindings.clear();
        this.registeredListeners.clear();
    }
}
