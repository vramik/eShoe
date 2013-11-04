package com.issuetracker.pages.validator;

import com.issuetracker.dao.api.ProjectDao;
import javax.inject.Inject;
import net.ftlines.wicket.cdi.CdiContainer;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;


/**
 *
 * @author mgottval
 */
public class ProjectNameValidator implements IValidator<String>{

    @Inject
    private ProjectDao projectDao;
    
    public ProjectNameValidator() {
        CdiContainer.get().getNonContextualManager().inject(this);
    }
    
        public void validate(IValidatable<String> iv) {
        String projectName = iv.getValue(); 
        Boolean b = projectDao.isProjectNameInUse(projectName);
//        Logger.getLogger(UsernameValidator.class.getName()).log(Level.SEVERE, b.toString());
        if (b) {
            error(iv, "usernameAlreadyExists");
        }
    }
    private void error(IValidatable<String> validatable, String errorKey) {
        ValidationError error = new ValidationError();
        error.addKey(getClass().getSimpleName()+ "." + errorKey);
        validatable.error(error);
    }
    
    
}
