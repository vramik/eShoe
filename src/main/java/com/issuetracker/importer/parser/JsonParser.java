package com.issuetracker.importer.parser;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.issuetracker.importer.model.BugzillaBugResponse;
import com.issuetracker.importer.model.BugzillaComment;
import com.issuetracker.importer.model.BugzillaCommentResponse;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Jirka
 * Date: 11.12.13
 * Time: 12:25
 * To change this template use File | Settings | File Templates.
 */
public class JsonParser implements Parser {

    public <T> T parse(String input, Class<T> clazz) {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        
        T output = null;

        try {            
            output = mapper.<T>readValue(input, clazz);
        } catch (IOException e) {
            //TODO: change this
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return output;
    }
}