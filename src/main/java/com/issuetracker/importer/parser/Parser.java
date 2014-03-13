package com.issuetracker.importer.parser;

/**
 * Responsible for parsing string input and returning parsed entity object.
 *
 * @author Jiri Holusa
 */
public interface Parser {

    /**
     * Parses input into entity of class "clazz".
     *
     * @param input string to be parsed
     * @param clazz output class of returned entity
     * @param <T> type of the entity
     * @return parsed entity of gived class
     */
    public <T> T parse(String input, Class<T> clazz);
}
