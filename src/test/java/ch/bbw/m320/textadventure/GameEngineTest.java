package ch.bbw.m320.textadventure;

import ch.bbw.m320.textadventure.model.GameState;
import ch.bbw.m320.textadventure.model.Story;
import ch.bbw.m320.textadventure.view.ConsoleView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Engine tests against the original tutorial story, with the console output captured.
 */
class GameEngineTest {

    private static final String ROOM_ONE_DESCRIPTION = """
            You are in an unadorned, rectangular room without windows and a table next to the wall.
            A simple light bulb is hanging from the ceiling, the only source of light, casting harsh shadows.
            There is a passage to the east, apparently to another room much like this.""";

    private static final String ROOM_TWO_DESCRIPTION = """
            You are in another unadorned, rectangular room.
            There is a passage to the west. In the opposite direction you see a large wardrobe.""";

    private static final String SECRET_DESCRIPTION =
            "You found the hidden passage to Narnia. The tutorial ends here. Congratulations!";

    private final Story story = new StoryLoader().load("tutorial.yaml");
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private GameState state;
    private GameEngine engine;

    @BeforeEach
    void setUp() {
        state = new GameState(story.startRoom());
        var view = new ConsoleView(new PrintStream(output, true, StandardCharsets.UTF_8));
        engine = new GameEngine(story, state, view);
    }

    private String console() {
        return output.toString(StandardCharsets.UTF_8).replace("\r\n", "\n").trim();
    }

    @Test
    void lookPrintsObjectMessage() {
        engine.execute("look", "table");

        assertEquals("This is a simple, wooden table. It has a small key lying on top.", console());
    }

    @Test
    void takeAddsStateAndSecondAttemptAcknowledgesIt() {
        engine.execute("take", "key");

        assertEquals("You pack it into your pocket.", console());
        assertTrue(state.hasState("key"));

        engine.execute("take", "key");

        assertEquals("You pack it into your pocket.\nYou already have the key safely in your pocket.", console());
    }

    @Test
    void conditionalActionWithoutStateRunsNextAction() {
        engine.execute("go", "east");
        engine.execute("open", "wardrobe");

        assertEquals(ROOM_TWO_DESCRIPTION + "\n\nYou try to pick up the wardrobe... Well... no.", console());
        assertFalse(state.hasState("wardrobeOpen"));
    }

    @Test
    void synonymResolvesToCanonicalVerb() {
        engine.execute("walk", "east");

        assertEquals(ROOM_TWO_DESCRIPTION, console());
        assertEquals("room-two", state.currentRoom());
    }

    @Test
    void roomSwitchPrintsDescriptionAndUpdatesState() {
        engine.execute("go", "east");

        assertEquals(ROOM_TWO_DESCRIPTION, console());
        assertEquals("room-two", state.currentRoom());
    }

    @Test
    void unknownObjectUsesGlobalTemplate() {
        engine.execute("go", "north");

        assertEquals("You can't go north from here!", console());
        assertEquals("room-one", state.currentRoom());
    }

    @Test
    void unknownVerbFallsBackToDefaultMessage() {
        engine.execute("fly", "up");

        assertEquals("You cannot do that.", console());
    }

    @Test
    void helpVerbsShowsUsageText() {
        engine.execute("help", "verbs");

        assertTrue(console().contains("Most of the time, typing something like <verb object> works."));
    }

    @Test
    void helpSynonymAloneShowsUsageText() {
        engine.execute("wtf", "");

        assertTrue(console().contains("Type <help verbs> to get a list of possible verbs."));
    }

    @Test
    void bareLookDescribesCurrentRoom() {
        engine.execute("look", "");

        assertEquals(ROOM_ONE_DESCRIPTION, console());
    }

    @Test
    void blockedActionWithoutMatchingAlternativeIsSilent() {
        engine.execute("go", "east");
        engine.execute("go", "east");

        assertEquals(ROOM_TWO_DESCRIPTION, console());
        assertEquals("room-two", state.currentRoom());
    }

    @Test
    void fullWalkthroughReachesSecretRoom() {
        engine.execute("take", "key");
        engine.execute("go", "east");
        engine.execute("open", "wardrobe");
        engine.execute("take", "coat");
        engine.execute("go", "east");

        assertEquals("secret", state.currentRoom());
        assertTrue(console().endsWith(SECRET_DESCRIPTION));
        assertTrue(state.hasState("coat"));
        assertTrue(state.hasState("wardrobeOpen"));
    }
}