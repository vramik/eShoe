///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.issuetracker.pages.component.version;
//
//import com.issuetracker.dao.api.ProjectVersionDao;
//import com.issuetracker.model.ProjectVersion;
//import java.util.List;
//import javax.inject.Inject;
//import org.apache.wicket.markup.html.basic.Label;
//import org.apache.wicket.markup.html.link.Link;
//import org.apache.wicket.markup.html.list.ListItem;
//import org.apache.wicket.markup.html.list.ListView;
//
///**
// *
// * @author mgottval
// */
//public class ProjectVersionListView extends ListView{
//
//
//        @Inject
//    private ProjectVersionDao projectVersionDao;
//    private List<ProjectVersion> projectVersionList;
//    
//    @Override
//    protected void populateItem(ListItem item) {
//        final ProjectVersion projectVersion = (ProjectVersion) item.getModelObject();
//                item.add(new Link<ProjectVersion>("remove", item.getModel()) {
//                    @Override
//                    public void onClick() {
//                        projectVersionList.remove(projectVersion);
//                        projectVersionDao.remove(projectVersion);
//                    }
//                });
//                item.add(new Label("name", projectVersion.getName()));
//    }
//    
//}
