package eu.psandro.tsjames.bot.query;

import eu.psandro.tsjames.model.PermissionFetcher;
import eu.psandro.tsjames.rank.RankPermission;
import lombok.Getter;

/**
 * @author PSandro on 29.07.18
 * @project tsjames
 */
public abstract class PermittedAction {
    @Getter
    private final PermissionFetcher permissionFetcher;

    protected PermittedAction(PermissionFetcher permissionFetcher) {
        this.permissionFetcher = permissionFetcher;
    }


    public abstract RankPermission[] getNeededPermissions();
}
