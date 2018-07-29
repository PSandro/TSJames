package eu.psandro.tsjames.bot.bootstrap;

import eu.psandro.tsjames.bot.model.FileConfigManager;

import java.io.IOException;

public final class Bootstrap {

    public static void main(String[] args) {
        try {
            new TSJamesBot(new FileConfigManager());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
