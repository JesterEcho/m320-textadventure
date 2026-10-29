package ch.bbw.m320.textadventure;

import ch.bbw.m320.textadventure.model.SaveGame;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Persists save games as YAML files, one file per slot.
 */
public class SaveGameManager {

    private static final Pattern SLOT_PATTERN = Pattern.compile("[a-z0-9-]+");

    private final Path directory;
    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    public SaveGameManager() {
        this(Path.of("saves"));
    }

    /**
     * Creates a manager storing save games in the given directory.
     */
    public SaveGameManager(Path directory) {
        this.directory = directory;
    }

    /**
     * Only lowercase letters, digits and hyphens are valid slot names.
     */
    public boolean isValidSlot(String slot) {
        return slot != null && SLOT_PATTERN.matcher(slot).matches();
    }

    /**
     * Writes the snapshot to the slot file.
     */
    public void save(SaveGame game, String slot) {
        try {
            Files.createDirectories(directory);
            mapper.writeValue(path(slot).toFile(), game);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot write save game: " + slot, e);
        }
    }

    /**
     * Reads the snapshot from the slot file, if one exists.
     */
    public Optional<SaveGame> load(String slot) {
        var file = path(slot).toFile();
        if (!file.isFile()) {
            return Optional.empty();
        }
        try {
            return Optional.of(mapper.readValue(file, SaveGame.class));
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read save game: " + slot, e);
        }
    }

    private Path path(String slot) {
        return directory.resolve(slot + ".yaml");
    }
}