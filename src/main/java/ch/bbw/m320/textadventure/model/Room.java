package ch.bbw.m320.textadventure.model;

import java.util.Map;

/**
 * A single location of the story with its room-specific verbs.
 *
 * @param name        display name of the room
 * @param description text printed when the room is entered
 * @param verbs       room-specific verbs, mapped from verb name to objects to actions
 */
public record Room(String name, String description, Map<String, Map<String, java.util.List<Action>>> verbs) {
}