package eu.psandro.tsjames.io.auth;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author PSandro on 25.08.18
 * @project tsjames
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public final class NetSubject {

    private final @NonNull
    int id;


}
