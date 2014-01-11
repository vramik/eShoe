package com.issuetracker.importer.parser;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.issuetracker.importer.parser.Parser;
import com.issuetracker.importer.model.BugzillaBug;
import com.issuetracker.importer.model.BugzillaResponse;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jirka
 * Date: 11.12.13
 * Time: 12:25
 * To change this template use File | Settings | File Templates.
 */
public class JsonParser implements Parser {

    public List<BugzillaBug> parse(String input) {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        
        BugzillaResponse output = null;
        
        try {            
            output = mapper.readValue(input, BugzillaResponse.class);
        } catch (IOException e) {
            //TODO: change this
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        List<BugzillaBug> bugs = output.getBugs();
        return bugs;
    }
}
