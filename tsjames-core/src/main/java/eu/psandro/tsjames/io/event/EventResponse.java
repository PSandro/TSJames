package eu.psandro.tsjames.io.event;


import eu.psandro.tsjames.io.protocol.NetPacket;
import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Getter

public class EventResponse {

    @AllArgsConstructor
    @Getter
    public enum Priority {
        LOWEST(-3),
        LOWER(-2),
        LOW(-1),
        NORMAL(0),
        HIGH(1),
        HIGHER(2),
        HIGHEST(3);

        final int priorityValue;
    }

    @NonNull
    private final NetPacket netPacket;

    private int priority = Priority.NORMAL.getPriorityValue();

    public EventResponse(NetPacket netPacket, Priority priority) {
        this.netPacket = netPacket;
        this.priority = priority.getPriorityValue();
    }

}
