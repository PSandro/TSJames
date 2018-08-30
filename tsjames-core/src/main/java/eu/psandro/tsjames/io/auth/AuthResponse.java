package eu.psandro.tsjames.io.auth;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;


@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public final class AuthResponse {

    public static final byte MAGIC_NUMBER = 'B';


    private final String errorMessage;

    public final NetSubject local;

    public final NetSubject server;

    public boolean isSuccess() {
        return this.errorMessage == null || this.errorMessage.isEmpty();
    }

}
