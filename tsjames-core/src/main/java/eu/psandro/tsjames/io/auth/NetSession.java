package eu.psandro.tsjames.io.auth;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public final class NetSession {

    @NonNull
    private final NetSubject localSubject;

    @NonNull
    private final NetSubject serverSubject;

    @NonNull
    private final Date sessionStart;
}
