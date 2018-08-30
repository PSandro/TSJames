package eu.psandro.tsjames.controller.console;

import eu.psandro.tsjames.controller.console.command.CommandHandler;
import eu.psandro.tsjames.misc.Messages;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public final class ConsoleIO extends Thread {

    private final PrintStream outputStream;
    private final Scanner scanner;
    @Getter
    @Setter
    private @NonNull
    CommandHandler commandHandler;


    public ConsoleIO(@NonNull InputStream inputStream, @NonNull PrintStream outputStream) {
        this.outputStream = outputStream;
        this.scanner = new Scanner(inputStream);
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
                if (this.commandHandler != null) {
                    this.outputStream.println(this.commandHandler.handleCommandInput(args));
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
