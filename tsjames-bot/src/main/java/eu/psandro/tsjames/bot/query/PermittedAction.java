package eu.psandro.tsjames.bot.query;

/**
 * @author PSandro on 29.07.18
 * @project tsjames
 */
public abstract class PermittedAction {

    protected PermittedAction() {
    }


    public abstract String[] getNeededPermissions();
}
