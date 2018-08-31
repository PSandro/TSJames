package eu.psandro.tsjames.daemon.io;

import eu.psandro.tsjames.api.exception.JamesException;
import eu.psandro.tsjames.io.auth.NetSubject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author PSandro on 31.08.18
 * @project tsjames
 */
public final class SessionManager extends DefaultChannelGroup {

    private final AtomicInteger idCounter = new AtomicInteger(1);
    private final Map<Integer, ChannelId> idBindings = new HashMap<>();

    public SessionManager() {
        super(GlobalEventExecutor.INSTANCE);
    }

    public NetSubject registerSession(final Channel channel) {
        if (super.contains(channel) || this.idBindings.containsValue(channel.id()))
            throw new JamesException("Channel is already registered!");

        final int id = this.idCounter.getAndIncrement();
        super.add(channel);
        this.idBindings.put(id, channel.id());
        return NetSubject.byId(id);
    }


    public Channel getSession(int id) {
        if (!this.idBindings.containsKey(id)) return null;
        final Channel channel = super.find(this.idBindings.get(id));
        if (channel == null) this.idBindings.remove(id);
        return channel;
    }

}
