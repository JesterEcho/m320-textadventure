package ch.bbw.m320.textadventure.view;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * Console view: prints messages and reads player input. Part of the view layer.
 */
public class ConsoleView {

    private final PrintStream out;
    private final Scanner scanner = new Scanner(System.in);

    @SuppressWarnings("java:S106")
    public ConsoleView() {
        this(System.out);
    }

    /**
     * Creates a view writing to the given stream, used for tests and other sinks.
     */
    public ConsoleView(PrintStream out) {
        this.out = out;
    }

    public void print(String message) {
        if (message != null && !message.isBlank()) {
            out.println(message);
        }
    }

    public void showRoom(String roomName, String description) {
        out.println();
        out.println("--- " + roomName + " ---");
        out.println(description);
    }

    /**
     * Reads one line of input and splits it into verb and optional object.
     */
    public Input readInput() {
        out.print("> ");
        var line = scanner.nextLine().trim().toLowerCase();
        var parts = line.split("\\s+", 2);
        return new Input(parts[0], parts.length > 1 ? parts[1] : "");
    }

    /**
     * Parsed player input: the raw verb word and the optional object word.
     */
    public record Input(String verb, String object) {
    }
}