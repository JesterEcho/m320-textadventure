package ch.bbw.m320.textadventure.model;

import java.util.Map;

/**
 * Immutable data model of the YAML story format, deserialized with Jackson.
 *
 * @param startRoom   key of the room the player starts in, must match a key in rooms
 * @param description intro text of the story
 * @param rooms       all rooms of the story, keyed by their internal name
 * @param globals     global verb configurations, keyed by verb name; "default" holds the fallback
 */
public record Story(String startRoom, String description, Map<String, Room> rooms,
                    Map<String, GlobalVerb> globals) {

    public Room room(String key) {
        var room = rooms.get(key);
        if (room == null) {
            throw new IllegalArgumentException("Unknown room key: " + key);
        }
        return room;
    }

    /**
     * Returns the configuration of a verb, falling back to the default configuration.
     */
    public GlobalVerb globalVerb(String verb) {
        var config = globals.get(verb);
        return config != null ? config : globals.get("default");
    }
}