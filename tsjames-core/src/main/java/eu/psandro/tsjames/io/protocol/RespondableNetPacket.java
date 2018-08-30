package eu.psandro.tsjames.io.protocol;

import lombok.*;

@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class RespondableNetPacket extends NetPacket {

    @Setter
    @Getter
    private ResponseCall listener;


}
