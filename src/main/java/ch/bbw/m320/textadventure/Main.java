package ch.bbw.m320.textadventure;

import ch.bbw.m320.textadventure.model.GameState;
import ch.bbw.m320.textadventure.model.Story;
import ch.bbw.m320.textadventure.view.ConsoleView;

import java.io.UncheckedIOException;
import java.util.List;
import java.util.Locale;

/**
 * Application entry point: loads the story and runs the main loop.
 * Built-in commands (save, load, quit) are handled here, everything
 * else is delegated to the game engine.
 */
public class Main {

    private final Story story;
    private final GameState state;
    private final ConsoleView view;
    private final GameEngine engine;
    private final SaveGameManager saves = new SaveGameManager();

    public Main(Story story, GameState state, ConsoleView view) {
        this.story = story;
        this.state = state;
        this.view = view;
        this.engine = new GameEngine(story, state, view);
    }

    /**
     * Starts the game with the story given as the first argument, defaulting to tutorial.yaml.
     */
    public static void main(String[] args) {
        var story = new StoryLoader().load(storyResource(args));
        var state = new GameState(story.startRoom());
        var view = new ConsoleView();

        view.print(story.description());
        new Main(story, state, view).start();
    }

    private static String storyResource(String[] args) {
        var name = args.length > 0 ? args[0] : "tutorial";
        if (name.startsWith("--args=")) {
            name = name.substring("--args=".length());
        }
        return name.endsWith(".yaml") ? name : name + ".yaml";
    }

    private void start() {
        showCurrentRoom();
        var exit = false;
        while (!exit) {
            var input = view.readInput();
            if (isExit(input.verb())) {
                view.print("Goodbye!");
                exit = true;
            } else if (!input.verb().isBlank()) {
                handleCommand(input.verb(), input.object());
            }
        }
    }

    private void handleCommand(String verb, String object) {
        switch (verb) {
            case "save" -> saveGame(object);
            case "load" -> loadGame(object);
            default -> engine.execute(verb, object);
        }
    }

    private void saveGame(String slot) {
        if (!saves.isValidSlot(slot)) {
            view.print("Please choose a slot: save <slot> (lowercase letters, digits, hyphen).");
            return;
        }
        try {
            saves.save(engine.snapshot(), slot);
            view.print("Game saved in slot '" + slot + "'.");
        } catch (UncheckedIOException _) {
            view.print("Could not save the game.");
        }
    }

    private void loadGame(String slot) {
        if (!saves.isValidSlot(slot)) {
            view.print("Please choose a slot: load <slot> (lowercase letters, digits, hyphen).");
            return;
        }
        try {
            var save = saves.load(slot);
            if (save.isEmpty()) {
                view.print("No save game found in slot '" + slot + "'.");
                return;
            }
            view.print("Game loaded from slot '" + slot + "'.");
            engine.restore(save.get());
        } catch (UncheckedIOException _) {
            view.print("Could not load the game.");
        }
    }

    private void showCurrentRoom() {
        var room = story.room(state.currentRoom());
        view.showRoom(room.name(), room.description());
    }

    private static boolean isExit(String verb) {
        return List.of("quit", "exit").contains(verb.toLowerCase(Locale.ROOT));
    }
}