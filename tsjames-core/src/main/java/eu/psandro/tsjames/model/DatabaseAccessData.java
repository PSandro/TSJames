package eu.psandro.tsjames.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
@AllArgsConstructor
public final class DatabaseAccessData {

    private final String url;

    private final String username;

    private final String password;

}
