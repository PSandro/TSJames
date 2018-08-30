package eu.psandro.tsjames.io.auth;

import lombok.Data;
import lombok.NonNull;

@Data
public class AuthCredentials {

    @NonNull
    private final String user;

    @NonNull
    private final String pass;

}
