package com.issuetracker.importer;

<<<<<<< HEAD:src/main/java/com/issuetracker/importer/Importer.java

import com.issuetracker.importer.mapper.Mapper;
import com.issuetracker.importer.parser.Parser;
import com.issuetracker.importer.reader.Reader;
import com.issuetracker.importer.model.BugzillaBug;
=======
import com.issuetracker.importer.importer.Importer;
import com.issuetracker.importer.importer.ProjectVersionImporter;
import com.issuetracker.importer.parser.Parser;
import com.issuetracker.importer.reader.Reader;
import com.issuetracker.importer.model.BugzillaBug;

import java.util.ArrayList;
import java.util.List;
>>>>>>> fdc9f10... Importer structure evolved:src/main/java/com/issuetracker/importer/ImporterMain.java

/**
 * Created with IntelliJ IDEA.
 * User: Jirka
 * Date: 4.12.13
 * Time: 10:42
 * To change this template use File | Settings | File Templates.
 */
public class ImporterMain {

    private Reader reader;
    private Parser parser;

    public static void main(String[] args) {
        new ImporterMain().run();
    }

    private void run() {
        /*reader = new RestReader();
        parser = new JsonParser();

        String readResult = reader.read();
        List<BugzillaBug> result = parser.parse(readResult);

        System.out.println(result);*/


        BugzillaBug bug = new BugzillaBug();
        bug.setSummary("Summary value");

        List<String> versionList = new ArrayList<String>();
        versionList.add("6.2.0");
        bug.setVersion(versionList);

        //bug.setComponent("Component value");
        bug.setCreator("Creator value");

        Importer importer = new ProjectVersionImporter();


        importer.process(bug);
    }

}
