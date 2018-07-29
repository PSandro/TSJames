package eu.psandro.tsjames.rank;

import lombok.NonNull;

/**
 * @author PSandro on 29.07.18
 * @project tsjames
 */
public final class PermissionFactory {

    public static RankPermission createPermission(@NonNull String name) {
        return new RankPermission(name);
    }

}
