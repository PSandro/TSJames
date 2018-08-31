package eu.psandro.tsjames.daemon.bootstrap;

import eu.psandro.tsjames.bootstrap.JamesConsoleBootstrap;
import eu.psandro.tsjames.daemon.io.AbstractNetServer;
import eu.psandro.tsjames.daemon.io.NetServerImpl;

import java.io.IOException;

/**
 * @author PSandro on 30.08.18
 * @project tsjames
 */
public final class JamesDeamon extends JamesConsoleBootstrap {

    private AbstractNetServer netServer;


    @Override
    public boolean init() {
        if (!super.init()) return false;
        super.getConsole().printLine("Initializing JamesDeamon version " + this.getVersion());
        super.getConsole().printSpace(2);

        try {
            this.netServer = new NetServerImpl(8000); //TODO persist port in file
            super.getConsole().printLine("Initializing NetServer...");
            this.netServer.init();
            super.getConsole().printLine("Booting NetServer...");
            boolean success = this.netServer.establish();
            super.getConsole().printLine("NetServer: " + (success ? "Success!" : "Failed!"));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


        return true;
    }


    private String getVersion() {
        return this.getClass().getPackage().getImplementationVersion();
    }

    @Override
    public void prepareShutdown() {
        super.prepareShutdown();
        try {
            this.netServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.netServer = null;
    }
}
