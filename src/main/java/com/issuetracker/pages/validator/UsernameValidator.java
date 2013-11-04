package com.issuetracker.pages.validator;

import com.issuetracker.dao.api.UserDao;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import net.ftlines.wicket.cdi.CdiContainer;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

/**
 *
 * @author mgottval
 */
public class UsernameValidator implements IValidator<String>{

    @Inject
    private UserDao userDao;   
    
    public UsernameValidator() {
        CdiContainer.get().getNonContextualManager().inject(this);
    }
    
    @Override
    public void validate(IValidatable<String> iv) {
        String username = iv.getValue(); 
        Boolean b = userDao.isUsernameInUse(username);
        Logger.getLogger(UsernameValidator.class.getName()).log(Level.SEVERE, b.toString());
        if (userDao.isUsernameInUse(username)) {
            error(iv, "usernameAlreadyExists");
        }
    }
    private void error(IValidatable<String> validatable, String errorKey) {
        ValidationError error = new ValidationError();
        error.addKey(getClass().getSimpleName()+ "." + errorKey);
        validatable.error(error);
    }
    
}
