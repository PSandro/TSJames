package eu.psandro.tsjames.bot.controller;

import eu.psandro.tsjames.bot.bootstrap.TSJamesBot;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public abstract class Command {

    @Getter(AccessLevel.PROTECTED)
    private final TSJamesBot jamesBot;

    public abstract String handleCommand(String[] args);

}
