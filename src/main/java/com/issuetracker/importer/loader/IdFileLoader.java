package com.issuetracker.importer.loader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * //TODO: document this
 *
 * @author Jiří Holuša
 */
public class IdFileLoader {

    public List<String> loadIdsFromFile(String filename) {
        List<String> result = new ArrayList<String>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/" + filename)))){
            String line = null;
            while((line = br.readLine()) != null) {
                result.add(line);
            }
        } catch (FileNotFoundException e) {
            throw new IllegalStateException(e);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return result;
    }
}
