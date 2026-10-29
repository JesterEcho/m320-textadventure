package ch.bbw.m320.textadventure;

import ch.bbw.m320.textadventure.model.GameState;
import ch.bbw.m320.textadventure.model.SaveGame;
import ch.bbw.m320.textadventure.view.ConsoleView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifies saving and loading game progress as YAML snapshots.
 */
class SaveGameTest {

    private final Path saveDirectory;
    private final SaveGameManager manager;
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private GameEngine engine;

    SaveGameTest(@TempDir Path tempDir) {
        this.saveDirectory = tempDir;
        this.manager = new SaveGameManager(tempDir);
    }

    @BeforeEach
    void setUp() {
        var story = new StoryLoader().load("tutorial.yaml");
        var state = new GameState(story.startRoom());
        var view = new ConsoleView(new PrintStream(output, true, StandardCharsets.UTF_8));
        engine = new GameEngine(story, state, view);
    }

    private String console() {
        return output.toString(StandardCharsets.UTF_8).replace("\r\n", "\n").trim();
    }

    @Test
    void snapshotCapturesProgress() {
        engine.execute("take", "key");
        engine.execute("go", "east");

        var snapshot = engine.snapshot();

        assertEquals("room-two", snapshot.currentRoom());
        assertEquals("key", snapshot.states().getFirst());
    }

    @Test
    void restoreReplaysProgressAndShowsRoom() {
        var save = new SaveGame("room-two", List.of("key", "wardrobeOpen"));

        engine.restore(save);

        assertEquals("room-two", engine.snapshot().currentRoom());
        assertTrue(console().startsWith("You are in another unadorned, rectangular room."));
    }

    @Test
    void saveAndLoadRoundTripsThroughYaml() throws Exception {
        var save = new SaveGame("room-two", List.of("coat", "key"));
        manager.save(save, "slot-1");

        var yaml = Files.readString(saveDirectory.resolve("slot-1.yaml"));
        assertTrue(yaml.contains("currentRoom"));
        assertTrue(yaml.contains("room-two"));
        assertEquals(save, manager.load("slot-1").orElseThrow());
    }

    @Test
    void loadMissingSlotIsEmpty() {
        assertTrue(manager.load("nothing").isEmpty());
    }

    @Test
    void invalidSlotsAreRejected() {
        assertFalse(manager.isValidSlot(""));
        assertFalse(manager.isValidSlot("../escape"));
        assertFalse(manager.isValidSlot("Upper Case"));
        assertFalse(manager.isValidSlot(null));
        assertTrue(manager.isValidSlot("slot-1"));
    }
}