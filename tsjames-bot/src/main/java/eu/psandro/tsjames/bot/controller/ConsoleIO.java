package eu.psandro.tsjames.bot.controller;

import eu.psandro.tsjames.bot.controller.CommandHandler;
import eu.psandro.tsjames.bot.view.Messages;
import lombok.NonNull;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Optional;
import java.util.Scanner;

public final class ConsoleIO extends Thread {

    private final InputStream inputStream;
    private final PrintStream outputStream;
    private final Scanner scanner;
    private Optional<CommandHandler> commandHandler;


    public ConsoleIO(@NonNull InputStream inputStream, @NonNull PrintStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.scanner = new Scanner(inputStream);
    }


    public void setCommandHandler(@NonNull CommandHandler commandHandler) {
        this.commandHandler = Optional.ofNullable(commandHandler);
    }

    @Override
    public void run() {
        this.resetInput();
        while (this.scanner.hasNextLine()) {
            final String[] args = this.scanner.nextLine().split(" ");

            //Empty inputs
            if (args.length <= 0) {
                continue;
            } else { //Non empty inputs -> CommandHandler
                if (this.commandHandler.isPresent()) {
                    this.outputStream.println(this.commandHandler.get().handleCommandInput(args));
                } else {
                    this.outputStream.println(Messages.CONSOLE_NO_CMDHANDLER);
                }
            }
            this.resetInput();

        }
    }

    public void printLine(String s) {
        this.outputStream.println(s);
    }

    public void printSpace(int lines) {
        for (int i = 0; i < lines; i++)
            this.outputStream.println();
    }

    public void printLineReset(String s) {
        this.outputStream.println(s);
        this.resetInput();
    }

    private void resetInput() {
        this.outputStream.print(Messages.CONSOLE_UI_PREFIX);
    }
}
