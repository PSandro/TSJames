package eu.psandro.tsjames.daemon.bootstrap;

import eu.psandro.tsjames.bootstrap.JamesConsoleBootstrap;

/**
 * @author PSandro on 30.08.18
 * @project tsjames
 */
public final class JamesDeamon extends JamesConsoleBootstrap {

    @Override
    public boolean init() {
        if (!super.init()) return false;
        super.getConsole().printLine("Initializing JamesDeamon version " + this.getVersion());
        super.getConsole().printSpace(2);


        return true;
    }


    private String getVersion() {
        return this.getClass().getPackage().getImplementationVersion();
    }
}
