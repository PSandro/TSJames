package eu.psandro.tsjames.bot.bootstrap;

import eu.psandro.tsjames.model.file.FileConfigManager;

public final class Bootstrap {

    public static void main(String[] args) {
        new TSJamesBot(new FileConfigManager())
                .bootstrap();
    }

}
