package eu.psandro.tsjames.io.event;

import eu.psandro.tsjames.io.protocol.NetPacket;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class RespondableNetPacketEvent<T extends NetPacket> extends NetPacketEvent<T> {

    @NonNull
    @Getter
    private final int responseId;


    private final SortedSet<EventResponse> response = new TreeSet<>(Comparator.comparingInt(EventResponse::getPriority));

    public void respond(@NonNull NetPacket netPacket) {
        this.response.add(new EventResponse(netPacket, EventResponse.Priority.NORMAL));
    }

    public void respond(@NonNull NetPacket netPacket, int priority) {
        this.response.add(new EventResponse(netPacket, priority));
    }

    public void respond(@NonNull NetPacket netPacket, @NonNull EventResponse.Priority priority) {
        this.response.add(new EventResponse(netPacket, priority.getPriorityValue()));
    }

    public void respond(EventResponse eventResponse) {
        this.response.add(eventResponse);
    }

    protected SortedSet<EventResponse> getResponse() {
        return this.response;
    }


}
