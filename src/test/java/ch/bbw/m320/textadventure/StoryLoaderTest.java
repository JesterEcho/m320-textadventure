package ch.bbw.m320.textadventure;

import ch.bbw.m320.textadventure.model.Story;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifies that the original tutorial story is deserialized completely.
 */
class StoryLoaderTest {

    private final Story story = new StoryLoader().load("tutorial.yaml");

    @Test
    void loadsStartRoomAndDescription() {
        assertEquals("room-one", story.startRoom());
        assertTrue(story.description().startsWith("Welcome to the tutorial!"));
    }

    @Test
    void loadsAllRooms() {
        assertEquals(3, story.rooms().size());
        assertTrue(story.rooms().containsKey("room-one"));
        assertTrue(story.rooms().containsKey("room-two"));
        assertTrue(story.rooms().containsKey("secret"));
    }

    @Test
    void loadsRoomVerbsAndActions() {
        var roomTwo = story.rooms().get("room-two");
        assertEquals(4, roomTwo.verbs().size());
        var wardrobeActions = roomTwo.verbs().get("open").get("wardrobe");
        assertEquals(2, wardrobeActions.size());
        assertEquals("key", wardrobeActions.getFirst().ifState());
    }

    @Test
    void loadsGlobalVerbsWithSynonyms() {
        assertEquals(6, story.globals().size());
        assertTrue(story.globals().get("go").synonyms().contains("walk"));
        assertEquals(story.globals().get("default"), story.globalVerb("missing-verb"));
    }

    @Test
    void unknownRoomKeyThrows() {
        assertThrows(IllegalArgumentException.class, () -> story.room("narnia"));
    }
}