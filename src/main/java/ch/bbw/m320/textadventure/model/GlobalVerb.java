package ch.bbw.m320.textadventure.model;

import java.util.List;

/**
 * Global verb configuration: synonyms and error messages.
 *
 * @param synonyms      alternative words the player may type for this verb
 * @param unknownVerb   message if the room does not know the verb at all
 * @param unknownObject message if the room knows the verb but not the object, {} is replaced by the object
 */
public record GlobalVerb(List<String> synonyms, String unknownVerb, String unknownObject) {
}