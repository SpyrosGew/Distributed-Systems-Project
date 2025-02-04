package gr.hua.dit.ds.ds_lab_2024.dao;
import gr.hua.dit.ds.ds_lab_2024.entities.Application;
import gr.hua.dit.ds.ds_lab_2024.entities.Property;
import gr.hua.dit.ds.ds_lab_2024.entities.Renter;
import java.util.List;

public interface PropertyDAO {
    public List<Application> getPropertiesApplications(int id);
    public List<Renter> getPropertiesRenters(int id);
    List<Property> findByFilters(String city, Integer minPrice, Integer maxPrice, String type);
}
