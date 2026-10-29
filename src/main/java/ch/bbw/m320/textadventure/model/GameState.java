package ch.bbw.m320.textadventure.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Mutable game state: the current room and the set of collected states.
 */
public class GameState {

    private final Set<String> states = new HashSet<>();
    private String currentRoom;

    public GameState(String startRoom) {
        this.currentRoom = startRoom;
    }

    public String currentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(String room) {
        this.currentRoom = room;
    }

    public boolean hasState(String state) {
        return states.contains(state);
    }

    public void addState(String state) {
        states.add(state);
    }

    public Set<String> states() {
        return Set.copyOf(states);
    }

    /**
     * Replaces room and states with the given snapshot.
     */
    public void restore(String room, Collection<String> newStates) {
        this.currentRoom = room;
        states.clear();
        states.addAll(newStates);
    }
}