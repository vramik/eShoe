package com.issuetracker.importer.parser;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Implementation of Parser which parses JSON string.
 *
 * @author Jiri Holusa
 */
public class JsonParser implements Parser {

    @Override
    public <T> T parse(String input, Class<T> clazz) {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        
        T output = null;

        try {            
            output = mapper.<T>readValue(input, clazz);
        } catch (IOException e) {
            throw new IllegalStateException("Error occurred while parsion JSON string into " + clazz.getName() + " entity.", e);
        }

        return output;
    }
}