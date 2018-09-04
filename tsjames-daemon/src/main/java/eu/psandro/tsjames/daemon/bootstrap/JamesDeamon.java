package eu.psandro.tsjames.daemon.bootstrap;

import eu.psandro.tsjames.bootstrap.JamesConsoleBootstrap;
import eu.psandro.tsjames.daemon.controller.command.ServerPingCommand;
import eu.psandro.tsjames.daemon.io.NetServerImpl;

/**
 * @author PSandro on 30.08.18
 * @project tsjames
 */
public final class JamesDeamon extends JamesConsoleBootstrap {

    private NetServerImpl netServer;


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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        super.getConsole().getCommandHandler()
                .registerCommand("ping", new ServerPingCommand(this.netServer));


        return true;
    }


    private String getVersion() {
        return this.getClass().getPackage().getImplementationVersion();
    }

    @Override
    public void prepareShutdown() {
        super.prepareShutdown();
        this.netServer.close();
        this.netServer = null;
    }
}
