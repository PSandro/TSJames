package eu.psandro.tsjames.model;

import eu.psandro.tsjames.rank.RankPermission;
import lombok.AllArgsConstructor;

/**
 * @author PSandro on 29.07.18
 * @project tsjames
 */
@AllArgsConstructor
public class PermissionFetcher {

    private final DatabaseManagerImpl databaseManager;

    public RankPermission getOrCreatePermission(String name) {
        return this.databaseManager.getOrCreatePermission(name);
    }


}
