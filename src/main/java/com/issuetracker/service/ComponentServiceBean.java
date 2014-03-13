package com.issuetracker.service;

import com.issuetracker.dao.api.ComponentDao;
import com.issuetracker.model.Component;
import com.issuetracker.service.api.ComponentService;

import javax.inject.Inject;
import java.util.List;

/**
 *
 * @author mgottval
 */
public class ComponentServiceBean implements ComponentService {

    @Inject
    private ComponentDao componentDao;

    @Override
    public void insert(Component component) {
        componentDao.insert(component);
    }

    @Override
    public List<Component> getComponents() {
        return componentDao.getComponents();
    }

    @Override
    public void remove(Component component) {
        componentDao.remove(component);
    }
}
