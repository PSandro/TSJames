package eu.psandro.tsjames.io.event;

import eu.psandro.tsjames.api.exception.JamesException;
import eu.psandro.tsjames.api.exception.JamesPacketAlreadyRegistered;
import eu.psandro.tsjames.io.protocol.NetPacket;
import lombok.NoArgsConstructor;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author PSandro on 26.08.18
 * @project tsjames
 */
@NoArgsConstructor
public class NetEventManager implements Closeable {
    private static final NetEventHandler[] EMPTYHANDLERS = {};

    private final Map<Class<? extends NetPacketEvent>, Collection<NetEventHandler>> bindings = new HashMap<>();
    private final Set<NetEventListener> registeredListeners = new HashSet<>();
    private final Map<Class<? extends NetPacket>, Class<? extends NetPacketEvent>> packetEventBindings = new HashMap<>();


    public NetEventHandler[] getListenersFor(Class<? extends NetPacketEvent> clazz) {
        final Collection<NetEventHandler> handlers = this.bindings.get(clazz);
        if (handlers == null || handlers.isEmpty())
            return EMPTYHANDLERS;
        return handlers.toArray(new NetEventHandler[handlers.size()]);
    }

    public <T extends NetPacketEvent> T executePacketEvent(T packetEvent) {
        final Collection<NetEventHandler> handlers = this.bindings.get(packetEvent.getClass());
        if (handlers == null) {
            System.out.println("no handlers found for packet " + packetEvent.getClass().getSimpleName());
            return packetEvent;
        }
        handlers.forEach(handler -> handler.execute(packetEvent));
        return packetEvent;
    }

    public void executePacketToEvent(NetPacket netPacket) {

    }

    public void registerListener(final NetEventListener listener) throws JamesException {

        final Method[] methods = listener.getClass().getDeclaredMethods();
        this.registeredListeners.add(listener);

        final Set<JamesException> exceptions = new HashSet<>();

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

            if (NetPacketEvent.class.isAssignableFrom(param)) {
                final Class<? extends NetPacketEvent> eventClazz = (Class<? extends NetPacketEvent>) param;


                final Type genericSuperclass = eventClazz.getGenericSuperclass();

                if (!(genericSuperclass instanceof ParameterizedType)) {
                    continue;
                }
                final ParameterizedType parameterizedType = (ParameterizedType) eventClazz.getGenericSuperclass();

                if (parameterizedType.getActualTypeArguments().length < 1) continue;

                final Class<? extends NetPacket> packetClazz = (Class<? extends NetPacket>) parameterizedType.getActualTypeArguments()[0].getClass();

                if (this.packetEventBindings.containsKey(packetClazz)) {
                    exceptions.add(new JamesPacketAlreadyRegistered(packetClazz));
                    continue;
                }

                this.packetEventBindings.put(packetClazz, eventClazz);

                if (!this.bindings.containsKey(eventClazz)) {
                    this.bindings.put(eventClazz, new TreeSet<>());
                }
                final Collection<NetEventHandler> eventHandlersForEvent = this.bindings.get(eventClazz);
                final NetEventHandler eventHandler = createEventHandler(listener, method, annotation);
                eventHandlersForEvent.add(eventHandler);
            }

            for (JamesException throwable : exceptions) {
                throw throwable;
            }
        }
    }

    private NetEventHandler createEventHandler(final NetEventListener listener, final Method method, final NetEvent annotation) {
        return new NetEventHandler(listener, method, annotation);
    }


    public void removeListener(NetEventListener listener) {
        for (Map.Entry<Class<? extends NetPacketEvent>, Collection<NetEventHandler>> ee : bindings.entrySet()) {
            ee.getValue().removeIf(curr -> curr.getListener() == listener);
        }
        this.registeredListeners.remove(listener);
    }

    public Map<Class<? extends NetPacketEvent>, Collection<NetEventHandler>> getBindings() {
        return new HashMap<>(this.bindings);
    }

    public Set<NetEventListener> getRegisteredListeners() {
        return new HashSet<>(this.registeredListeners);
    }

    @Override
    public void close() throws IOException {
        this.bindings.clear();
        this.registeredListeners.clear();
        this.packetEventBindings.clear();
    }
}
