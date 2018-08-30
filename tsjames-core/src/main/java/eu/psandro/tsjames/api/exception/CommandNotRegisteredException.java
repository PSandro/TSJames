package eu.psandro.tsjames.api.exception;

import eu.psandro.tsjames.api.exception.JamesException;

/**
 * @author PSandro on 29.07.18
 * @project tsjames
 */
public class CommandNotRegisteredException extends JamesException {

    public CommandNotRegisteredException(String label) {
        super("A command with the label " + label + " has not been registered!");
    }
}
