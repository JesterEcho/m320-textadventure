package ch.bbw.m320.textadventure;

import ch.bbw.m320.textadventure.model.Story;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

/**
 * Loads a story from a YAML file on the classpath or file system.
 */
public class StoryLoader {

    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    /**
     * Loads a story from a resource on the classpath.
     */
    public Story load(String resource) {
        var stream = getClass().getClassLoader().getResourceAsStream(resource);
        if (stream == null) {
            throw new IllegalArgumentException("Story not found: " + resource);
        }
        try (InputStream input = stream) {
            return mapper.readValue(input, Story.class);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read story: " + resource, e);
        }
    }
}