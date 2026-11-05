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
 * Walkthroughs of the classic Dragon Ball hunt: collect all seven Dragon
 * Balls, pass the Hall of Seven Stars, take the seventh ball from Pilaf
 * and summon Shenron. Puzzle gates must block with a hint and always
 * leave a way back - no dead ends.
 */
class DragonBallStoryTest {

    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private Story story;
    private GameState state;
    private GameEngine engine;

    @BeforeEach
    void setUp() {
        story = new StoryLoader().load("dragonball.yaml");
        state = new GameState(story.startRoom());
        var view = new ConsoleView(new PrintStream(output, true, StandardCharsets.UTF_8));
        engine = new GameEngine(story, state, view);
    }

    private String console() {
        return output.toString(StandardCharsets.UTF_8).replace("\r\n", "\n").trim();
    }

    private void play(String verb, String object) {
        engine.execute(verb, object);
    }

    private void takeTwoStar() {
        play("go", "south");
        play("take", "radar");
        play("go", "west");
        play("fight", "goons");
        play("fight", "boss");
        play("take", "ball");
    }

    private void takeThreeStar() {
        play("go", "east");
        play("go", "east");
        play("look", "turtle");
        play("take", "nimbus");
        play("go", "east");
        play("learn", "kamehameha");
        play("take", "ball");
        play("go", "west");
    }

    private void takeFiveStar() {
        play("go", "west");
        play("go", "south");
        play("fight", "yamcha");
        play("go", "south");
        play("take", "ball");
        play("go", "north");
        play("go", "north");
    }

    private void takeSixStar() {
        play("go", "north");
        play("go", "north");
        play("go", "north");
        play("take", "ball");
        play("go", "south");
        play("go", "south");
        play("go", "south");
    }

    private void takeOneStar() {
        play("go", "west");
        play("go", "west");
        play("look", "tower");
        play("go", "north");
        play("fight", "mai");
        play("take", "ball");
    }

    private void collectAllBalls() {
        play("take", "ball");
        takeTwoStar();
        takeThreeStar();
        takeFiveStar();
        takeSixStar();
        takeOneStar();
    }

    private void passHallOfStars() {
        play("go", "north");
        play("go", "north");
        play("go", "north");
        play("go", "north");
        play("go", "north");
        play("go", "north");
        play("go", "north");
    }

    @Test
    void storyLoadsWithAllRoomsAndGlobals() {
        assertEquals("goku-home", story.startRoom());
        assertEquals(20, story.rooms().size());
        assertEquals(8, story.globals().size());
    }

    @Test
    void storySpecificSynonymWorks() {
        play("go", "south");
        play("walk", "west");

        assertEquals("rabbit-village", state.currentRoom());
    }

    @Test
    void fullWalkthroughReachesTheWishEnding() {
        collectAllBalls();
        passHallOfStars();
        play("fight", "pilaf");
        play("summon", "dragon");

        assertEquals("the-wish", state.currentRoom());
        assertTrue(state.hasState("four-star"));
        assertTrue(state.hasState("two-star"));
        assertTrue(state.hasState("three-star"));
        assertTrue(state.hasState("five-star"));
        assertTrue(state.hasState("six-star"));
        assertTrue(state.hasState("one-star"));
        assertTrue(state.hasState("seven-star"));
        assertTrue(console().contains("THE END - The Wish."));
    }

    @Test
    void summonFailsOutsideTheThroneRoom() {
        play("summon", "dragon");
        play("go", "south");
        play("call", "dragon");

        assertEquals("bulma-camp", state.currentRoom());
        assertFalse(state.hasState("seven-star"));
        assertTrue(console().contains("There is nothing to summon here!"));
    }

    @Test
    void hallSocketsBlockAndHintTheMissingBall() {
        play("take", "ball");
        play("go", "south");
        play("take", "radar");
        play("go", "west");
        play("go", "west");
        play("look", "tower");
        play("go", "north");
        play("fight", "mai");
        play("take", "ball");
        play("go", "north");
        play("go", "north");
        play("go", "north");

        assertEquals("hall-of-stars-2", state.currentRoom());
        assertTrue(console().contains("The second socket stays dark."));

        play("go", "south");

        assertEquals("hall-of-stars-1", state.currentRoom());

        play("go", "south");

        assertEquals("treasury", state.currentRoom());
    }

    @Test
    void rabbitBossRequiresTheGoonFightFirst() {
        play("go", "south");
        play("go", "west");
        play("fight", "boss");

        assertFalse(state.hasState("boss-beaten"));
        assertTrue(console().contains("his goons dogpile you first"));

        play("fight", "goons");
        play("fight", "boss");
        play("take", "ball");

        assertTrue(state.hasState("two-star"));
    }

    @Test
    void roshiOnlyPartsWithTheBallForAStudent() {
        play("go", "south");
        play("go", "east");
        play("go", "east");
        play("look", "turtle");
        play("take", "nimbus");
        play("go", "east");
        play("take", "ball");

        assertFalse(state.hasState("three-star"));
        assertTrue(console().contains("Only a student leaves with the master's keepsake"));

        play("learn", "kamehameha");
        play("take", "ball");

        assertTrue(state.hasState("three-star"));
    }

    @Test
    void seaGateRequiresTheFlyingNimbus() {
        play("go", "south");
        play("go", "east");
        play("go", "east");

        assertEquals("turtle-beach", state.currentRoom());
        play("go", "east");

        assertEquals("turtle-beach", state.currentRoom());
        assertTrue(console().contains("No boat on this beach could cross it."));

        play("look", "turtle");
        play("take", "nimbus");
        play("go", "east");

        assertEquals("kame-house", state.currentRoom());
    }

    @Test
    void eternalFlameRequiresTheKamehameha() {
        play("go", "south");
        play("go", "north");
        play("go", "north");
        play("go", "north");

        assertEquals("fire-mountain", state.currentRoom());
        play("go", "north");

        assertEquals("fire-mountain", state.currentRoom());
        assertTrue(console().contains("Only the Turtle School's art could part an eternal fire."));

        play("go", "south");
        play("go", "south");
        play("go", "east");
        play("look", "turtle");
        play("take", "nimbus");
        play("go", "east");
        play("learn", "kamehameha");
        play("go", "west");
        play("go", "west");
        play("go", "north");
        play("go", "north");
        play("go", "north");
        play("go", "north");

        assertEquals("castle-hall", state.currentRoom());
    }

    @Test
    void yamchaGuardsTheCanyonUntilDefeated() {
        play("go", "south");
        play("go", "south");
        play("go", "south");

        assertEquals("desert", state.currentRoom());
        play("go", "south");

        assertEquals("desert", state.currentRoom());
        assertTrue(console().contains("the bandit bars the way"));

        play("fight", "yamcha");
        play("go", "south");

        assertEquals("yamcha-hideout", state.currentRoom());
    }

    @Test
    void hiddenVentRevealsTheTreasury() {
        play("go", "south");
        play("go", "west");
        play("go", "west");
        play("go", "north");

        assertEquals("wastelands", state.currentRoom());
        assertTrue(console().contains("No door, no window"));

        play("look", "tower");
        play("go", "north");

        assertEquals("treasury", state.currentRoom());
    }
}
