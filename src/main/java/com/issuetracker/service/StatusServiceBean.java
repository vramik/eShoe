package com.issuetracker.service;

import com.issuetracker.dao.api.StatusDao;
import com.issuetracker.model.Status;
import com.issuetracker.service.api.StatusService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

/**
 *
 * @author mgottval
 */
@Stateless
public class StatusServiceBean implements StatusService {

    @Inject
    private StatusDao statusDao;
    
    @Override
    public void insert(Status status) {
        statusDao.insert(status);
    }

    @Override
    public List<Status> getStatuses() {
        return statusDao.getStatuses();
    }

    @Override
    public void remove(Status status) {
        statusDao.remove(status);
    }

    @Override
    public Status getStatusById(Long id) {
        return statusDao.getStatusById(id);
    }

    @Override
    public Status getStatusByName(String name) {
        return statusDao.getStatusByName(name);
    }
}
