package eu.psandro.tsjames.bot.bootstrap;

import eu.psandro.tsjames.bot.model.ConfigManager;
import lombok.Getter;
import lombok.NonNull;


public final class TSJamesBot {

    @Getter
    private final ConfigManager configManager;

    private static final String ASCII_ART = "\n_________ _______  _______  _______  _______ \n\\__    _/(  ___  )(       )(  ____ \\(  ____ \\\n   )  (  | (   ) || () () || (    \\/| (    \\/\n   |  |  | (___) || || || || (__    | (_____ \n   |  |  |  ___  || |(_)| ||  __)   (_____  )\n   |  |  | (   ) || |   | || (            ) |\n|\\_)  )  | )   ( || )   ( || (____/\\/\\____) |\n(____/   |/     \\||/     \\|(_______/\\_______)\n      by Sandro P. (github.com/PSandro)      ";


    protected TSJamesBot(@NonNull ConfigManager configManager) {
        this.configManager = configManager;
        System.out.println(ASCII_ART);
    }


}
