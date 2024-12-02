package gr.hua.dit.ds.ds_lab_2024.service;

import gr.hua.dit.ds.ds_lab_2024.dao.OwnerDAOImpl;
import gr.hua.dit.ds.ds_lab_2024.entities.Application;
import gr.hua.dit.ds.ds_lab_2024.entities.Property;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OwnerService {

    private OwnerDAOImpl ownerDAO;

    @Transactional
    public List<Property> getOwnerProperties(Integer Id) {
        return ownerDAO.getOwnersProperties(Id);
    }

    @Transactional
    public List<Application> getOwnerApplications(Integer Id) {
        return ownerDAO.getOwnersApplications(Id);
    }
}
