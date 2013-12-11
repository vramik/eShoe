package com.issuetracker.importer;


import com.issuetracker.importer.parser.Parser;
import com.issuetracker.importer.reader.Reader;
import com.issuetracker.importer.parser.JsonParser;
import com.issuetracker.importer.reader.RestReader;
import com.issuetracker.importer.model.BugzillaBug;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jirka
 * Date: 4.12.13
 * Time: 10:42
 * To change this template use File | Settings | File Templates.
 */
public class Importer {

    private Reader reader;
    private Parser parser;

    public static void main(String[] args) {
        new Importer().run();
    }

    private void run() {
        reader = new RestReader();
        parser = new JsonParser();

        String readResult = reader.read();
        List<BugzillaBug> result = parser.parse(readResult);

        System.out.println(result);
    }

}
