package eu.psandro.tsjames.io.auth;

import java.util.Date;

public final class NetSessionFactory {

    public static NetSession createSession(final AuthResponse authResponse) {
        return new NetSession(authResponse.getLocal(), authResponse.getServer(), new Date());
    }

    public static AuthRequest createAuthRequest(final AuthCredentials authCredentials) {
        return new AuthRequest(authCredentials.getUser(), authCredentials.getPass());
    }

    public static NetSession createServerSession(int id) {
        final NetSubject subject = NetSubject.byId(id);
        return new NetSession(subject, subject, new Date());
    }
}
