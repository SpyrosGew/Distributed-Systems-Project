package gr.hua.dit.ds.ds_lab_2024.dao;

import gr.hua.dit.ds.ds_lab_2024.entities.Application;
import gr.hua.dit.ds.ds_lab_2024.entities.Property;

import java.util.List;

public interface RenterDAO {
    public List<Application> getRentersApplications(int renterId);
    public List<Property> getAcceptedProperties(int renterId);
}
