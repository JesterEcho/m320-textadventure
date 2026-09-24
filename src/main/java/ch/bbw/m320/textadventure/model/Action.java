package ch.bbw.m320.textadventure.model;

/**
 * One action in an action list. The properties are optional; only the first
 * matching action of the list is executed.
 *
 * @param ifState  only execute this action if the given state is set
 * @param addState adds the given state to the set of states
 * @param room     switches to the room with the given key
 * @param message  text printed to the player
 */
public record Action(String ifState, String addState, String room, String message) {
}