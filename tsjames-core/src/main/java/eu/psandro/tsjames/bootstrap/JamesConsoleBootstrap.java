package eu.psandro.tsjames.bootstrap;

import eu.psandro.tsjames.controller.console.ConsoleIO;
import eu.psandro.tsjames.controller.console.command.DefaultCommandHandler;
import eu.psandro.tsjames.misc.Messages;
import lombok.Getter;

/**
 * @author PSandro on 30.08.18
 * @project tsjames
 */
public abstract class JamesConsoleBootstrap extends JamesBootstrap {

    @Getter
    private final ConsoleIO console = new ConsoleIO(System.in, System.out);

    @Override
    public boolean init() {
        //Console IO setup
        this.console.setCommandHandler(new DefaultCommandHandler(this));

        //Print startup...
        this.console.printLine(Messages.JAMES_ASCII_ART);
        this.console.printSpace(2);

        return true;
    }

    @Override
    public void postInit() {
        //Enable Console Input
        this.console.printSpace(1);
        this.console.start();
    }

    @Override
    public void prepareShutdown() {
        this.console.printLine("bye...");
    }

    @Override
    public void shutdown() {
        this.prepareShutdown();
        System.exit(0);
    }
}
