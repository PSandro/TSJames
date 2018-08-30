package eu.psandro.tsjames.io.auth;


import lombok.*;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter(AccessLevel.PUBLIC)
@EqualsAndHashCode
public final class AuthRequest {

    public static final byte MAGIC_NUMBER = 'B';

    @NonNull
    private final String authUser;

    @NonNull
    private final String authPass;


}
