package com.issuetracker.pages;

import com.issuetracker.model.Status;
import com.issuetracker.model.Transition;
import com.issuetracker.model.Workflow;
import com.issuetracker.service.api.ImporterService;
import com.issuetracker.service.api.StatusService;
import com.issuetracker.service.api.TransitionService;
import com.issuetracker.service.api.WorkflowService;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
