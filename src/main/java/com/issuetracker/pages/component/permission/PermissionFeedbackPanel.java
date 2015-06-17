package com.issuetracker.pages.component.permission;

import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

/**
 *
 * @author vramik
 */
public class PermissionFeedbackPanel extends FeedbackPanel {
    
    private final MessageFilter filter = new MessageFilter();
        
    public PermissionFeedbackPanel(String id) {
        super(id);
        setFilter(filter);
    }

    public void clearMessages() {
        filter.clearMessages();
    }
    
    private class MessageFilter implements IFeedbackMessageFilter {
        
        private final List<FeedbackMessage> messages = new ArrayList<>();
        
        public void clearMessages() {
            messages.clear();
        }
        
        @Override
        public boolean accept(FeedbackMessage currentMessage) {
            for(FeedbackMessage message : messages){
                if(message.getMessage().toString().equals(currentMessage.getMessage().toString())) {
                    return false;
                }
            }
            messages.add(currentMessage);
            return true;
        }
        
    }
}
