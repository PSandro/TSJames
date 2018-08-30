package eu.psandro.tsjames.model.database;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
@AllArgsConstructor
public final class DatabaseAccessData {

    private final String url;

    private final String username;

    private final String password;


    public boolean isFilled() {
        return this.url != null && this.username != null && this.password != null;
    }

}
