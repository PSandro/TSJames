package eu.psandro.tsjames.bot.query.event;

import com.github.theholywaffle.teamspeak3.api.event.TS3Event;
import eu.psandro.tsjames.bot.query.PermittedAction;

/**
 * @author PSandro on 29.07.18
 * @project tsjames
 */
public abstract class TeamSpeakListener<E extends TS3Event> extends PermittedAction {

    protected TeamSpeakListener() {
        super();
    }

    public abstract void handle(E event);

}
