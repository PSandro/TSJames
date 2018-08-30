package eu.psandro.tsjames.io.auth;

import com.sun.istack.internal.Nullable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public final class AuthResponse {

    public static final byte MAGIC_NUMBER = 'B';


    @Nullable
    private final String errorMessage;

    @Nullable
    public final NetSubject local;

    public final NetSubject server;

    public boolean isSuccess() {
        return this.errorMessage == null || this.errorMessage.isEmpty();
    }

}
