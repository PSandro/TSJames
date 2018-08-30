package eu.psandro.tsjames.io.auth;

import lombok.*;

/**
 * @author PSandro on 25.08.18
 * @project tsjames
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Getter
public final class NetSubject {

    private final @NonNull
    int id;

    public static NetSubject byId(int id) {
        return new NetSubject(id);
    }

}
