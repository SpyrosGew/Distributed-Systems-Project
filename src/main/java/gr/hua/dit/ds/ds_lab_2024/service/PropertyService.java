package gr.hua.dit.ds.ds_lab_2024.service;

import gr.hua.dit.ds.ds_lab_2024.entities.Application;
import gr.hua.dit.ds.ds_lab_2024.entities.Property;
import gr.hua.dit.ds.ds_lab_2024.entities.Renter;
import gr.hua.dit.ds.ds_lab_2024.repositories.ApplicationRepository;
import gr.hua.dit.ds.ds_lab_2024.repositories.OwnerRepository;
import gr.hua.dit.ds.ds_lab_2024.repositories.PropertyRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.util.List;


@Service
public class PropertyService {

    private final StandardServletMultipartResolver standardServletMultipartResolver;
    private final ApplicationRepository applicationRepository;
    private PropertyRepository propertyRepository;
    private OwnerRepository ownerRepository;

    public PropertyService(PropertyRepository propertyRepository, OwnerRepository ownerRepository, StandardServletMultipartResolver standardServletMultipartResolver, ApplicationRepository applicationRepository){
        this.ownerRepository = ownerRepository;
        this.propertyRepository = propertyRepository;
        this.standardServletMultipartResolver = standardServletMultipartResolver;
        this.applicationRepository = applicationRepository;
    }

    @Transactional
    public List<Property> getProperties(){
        return propertyRepository.findAll();
    }

    @Transactional
    public void saveProperty(Property property){
        propertyRepository.save(property);
    }

    @Transactional
    public Property getProperty(Integer propertyId){
        return propertyRepository.findById(propertyId).get();
    }

    @Transactional
    public void setActiveRenter(int propertyId, Renter renter){
        Property property = propertyRepository.findById(propertyId).get();
        System.out.println(property);
        System.out.println(property.getActiveRenter());
        property.setActiveRenter(renter);
        System.out.println(property.getActiveRenter());
        propertyRepository.save(property);
    }

    @Transactional
    public void removeActiveRenter(int propertyId) {
        Property property = propertyRepository.findById(propertyId).get();
        property.setActiveRenter(null);
        propertyRepository.save(property);
    }

    @Transactional
    public void setActiveApplication(int propertyId, Application application){
        Property property = propertyRepository.findById(propertyId).get();
        System.out.println(property);
        System.out.println(property.getActiveApplication());
        property.setActiveApplication(application);
        System.out.println(property.getActiveApplication());
        propertyRepository.save(property);
    }

    @Transactional
    public void removeActiveApplication(int propertyId) {
        Property property = propertyRepository.findById(propertyId).get();
        property.setActiveApplication(null);
        propertyRepository.save(property);
    }

}
