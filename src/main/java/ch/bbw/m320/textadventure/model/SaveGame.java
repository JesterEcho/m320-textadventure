package ch.bbw.m320.textadventure.model;

import java.util.List;

/**
 * Serializable snapshot of a running game: the current room and the collected states.
 *
 * @param currentRoom key of the room the player is currently in
 * @param states      all states collected so far, sorted for stable file output
 */
public record SaveGame(String currentRoom, List<String> states) {
}