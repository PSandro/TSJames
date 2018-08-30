package eu.psandro.tsjames.io.auth;

import lombok.*;


@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public final class AuthResponse {

    public static final byte MAGIC_NUMBER = 'B';


    private final String errorMessage;

    public final NetSubject local;

    public final NetSubject server;

    public boolean isSuccess() {
        return this.errorMessage == null || this.errorMessage.isEmpty();
    }

}
