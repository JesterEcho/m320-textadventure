package ch.bbw.m320.textadventure;

import ch.bbw.m320.textadventure.model.Action;
import ch.bbw.m320.textadventure.model.GameState;
import ch.bbw.m320.textadventure.model.Story;
import ch.bbw.m320.textadventure.view.ConsoleView;

import java.util.List;
import java.util.Locale;

/**
 * Executes parsed player input against the story model and applies the resulting actions.
 * Pure controller logic without console access, so it is fully testable.
 */
public class GameEngine {

    private final Story story;
    private final GameState state;
    private final ConsoleView view;

    public GameEngine(Story story, GameState state, ConsoleView view) {
        this.story = story;
        this.state = state;
        this.view = view;
    }

    /**
     * Resolves the input words against synonyms and executes the matching action list.
     */
    public void execute(String verbInput, String objectInput) {
        var verb = resolveVerb(verbInput);
        if (verb == null) {
            view.print(story.globalVerb(verbInput.toLowerCase(Locale.ROOT)).unknownVerb());
            return;
        }
        if (isHelpRequest(verb, objectInput)) {
            view.print(story.globalVerb("help").unknownVerb());
            return;
        }
        if (objectInput.isBlank()) {
            executeWithoutObject(verb);
            return;
        }

        var room = story.room(state.currentRoom());
        var objects = room.verbs().get(verb);
        if (objects == null) {
            view.print(unknownVerbMessage(verb));
            return;
        }

        var actions = objects.get(objectInput);
        if (actions == null) {
            view.print(unknownObjectMessage(verb, objectInput));
            return;
        }
        runActions(actions);
    }

    /**
     * Handles input without an object: bare look describes the room,
     * all other verbs get the verb-level error message.
     */
    private void executeWithoutObject(String verb) {
        if ("look".equals(verb)) {
            enterRoom();
            return;
        }
        view.print(unknownVerbMessage(verb));
    }

    private String resolveVerb(String input) {
        var verb = input.toLowerCase(Locale.ROOT);
        for (var entry : story.globals().entrySet()) {
            var synonyms = entry.getValue().synonyms();
            if (synonyms != null && synonyms.contains(verb)) {
                return entry.getKey();
            }
        }
        return verb;
    }

    private boolean isHelpRequest(String verb, String object) {
        return "help".equals(verb) && "verbs".equalsIgnoreCase(object);
    }

    private String unknownVerbMessage(String verb) {
        var config = story.globalVerb(verb);
        return config != null && config.unknownVerb() != null
                ? config.unknownVerb()
                : story.globalVerb("default").unknownVerb();
    }

    private String unknownObjectMessage(String verb, String object) {
        var config = story.globalVerb(verb);
        if (config != null && config.unknownObject() != null) {
            return config.unknownObject().replace("{}", object);
        }
        var fallback = story.globalVerb("default");
        return fallback.unknownObject() != null
                ? fallback.unknownObject().replace("{}", object)
                : "";
    }

    /**
     * Runs only the first matching action of the list.
     */
    private void runActions(List<Action> actions) {
        for (var action : actions) {
            if (action.ifState() != null && !state.hasState(action.ifState())) {
                continue;
            }
            apply(action);
            return;
        }
    }

    private void apply(Action action) {
        if (action.addState() != null) {
            state.addState(action.addState());
        }
        if (action.message() != null) {
            view.print(action.message());
        }
        if (action.room() != null) {
            state.setCurrentRoom(action.room());
            enterRoom();
        }
    }

    private void enterRoom() {
        var room = story.room(state.currentRoom());
        view.print(room.description());
    }
}