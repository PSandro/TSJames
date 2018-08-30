package eu.psandro.tsjames.bootstrap;

/**
 * @author PSandro on 30.08.18
 * @project tsjames
 */
public abstract class JamesBootstrap {

    public void bootstrap() {
        if (this.init())
            this.postInit();
    }

    abstract boolean init();

    abstract void postInit();

    public abstract void prepareShutdown();

    public abstract void shutdown();

}
