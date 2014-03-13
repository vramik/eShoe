package com.issuetracker.importer.loader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads ID's from file.
 *
 * @author Jiri Holusa
 */
public class IdFileLoader {

    /**
     * Load ID's from file separated by newline and returns them as List.
     *
     * @param filename
     * @return
     */
    public List<String> loadIdsFromFile(String filename) {
        List<String> result = new ArrayList<String>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/" + filename)))){
            String line;
            while((line = br.readLine()) != null) {
                result.add(line);
            }
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("ID's file not found.", e);
        } catch (IOException e) {
            throw new IllegalStateException("Error occurred while reading the ID's file.", e);
        }

        return result;
    }
}
