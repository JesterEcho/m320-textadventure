package ch.bbw.m320.textadventure;

import ch.bbw.m320.textadventure.model.GameState;
import ch.bbw.m320.textadventure.model.Story;
import ch.bbw.m320.textadventure.view.ConsoleView;

import java.util.List;
import java.util.Locale;

/**
 * Application entry point: loads the story and runs the main loop.
 */
public class Main {

    public static void main(String[] args) {
        var loader = new StoryLoader();
        var story = loader.load("tutorial.yaml");
        var state = new GameState(story.startRoom());
        var view = new ConsoleView();

        view.print(story.description());
        enterCurrentRoom(story, state, view);

        var engine = new GameEngine(story, state, view);
        while (true) {
            var input = view.readInput();
            if (isQuit(input.verb())) {
                view.print("Goodbye!");
                return;
            }
            if (input.verb().isBlank()) {
                continue;
            }
            engine.execute(input.verb(), input.object());
        }
    }

    private static boolean isQuit(String verb) {
        return List.of("quit", "exit").contains(verb.toLowerCase(Locale.ROOT));
    }

    private static void enterCurrentRoom(Story story, GameState state, ConsoleView view) {
        var room = story.room(state.currentRoom());
        view.showRoom(room.name(), room.description());
    }
}