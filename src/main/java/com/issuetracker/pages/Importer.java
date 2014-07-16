package com.issuetracker.pages;

import com.issuetracker.service.api.ImporterService;

import javax.inject.Inject;

/**
 *
 * @author Jiri Holusa
 */
public class Importer extends PageLayout {

    @Inject
    private ImporterService importerService;

    public Importer() {
        importerService.doImport();
    }
}
