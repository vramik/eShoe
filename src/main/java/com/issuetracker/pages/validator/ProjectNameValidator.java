package com.issuetracker.pages.validator;

import com.issuetracker.service.api.ProjectService;
import net.ftlines.wicket.cdi.CdiContainer;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import javax.inject.Inject;

import javax.inject.Inject;

/**
 *
 * @author mgottval
 */
public class ProjectNameValidator implements IValidator<String> {

    @Inject
    private ProjectService projectService;

    public ProjectNameValidator() {
        CdiContainer.get().getNonContextualManager().inject(this);
    }

    @Override
    public void validate(IValidatable<String> iv) {
        String projectName = iv.getValue();
        Boolean b = projectService.isProjectNameInUse(projectName);
//        Logger.getLogger(UsernameValidator.class.getName()).log(Level.SEVERE, b.toString());
        if (b) {
            error(iv, "usernameAlreadyExists");
        }
    }

    private void error(IValidatable<String> validatable, String errorKey) {
        ValidationError error = new ValidationError();
        error.addKey(getClass().getSimpleName() + "." + errorKey);
        validatable.error(error);
    }

}
