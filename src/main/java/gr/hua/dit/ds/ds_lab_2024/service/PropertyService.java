package gr.hua.dit.ds.ds_lab_2024.service;

import gr.hua.dit.ds.ds_lab_2024.entities.*;
import gr.hua.dit.ds.ds_lab_2024.repositories.ApplicationRepository;
import gr.hua.dit.ds.ds_lab_2024.repositories.NotificationRepository;
import gr.hua.dit.ds.ds_lab_2024.repositories.OwnerRepository;
import gr.hua.dit.ds.ds_lab_2024.repositories.PropertyRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import gr.hua.dit.ds.ds_lab_2024.dao.PropertyDAO;

import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;


@Service
public class PropertyService {

    private final StandardServletMultipartResolver standardServletMultipartResolver;
    private final ApplicationRepository applicationRepository;
    private PropertyRepository propertyRepository;
    private OwnerRepository ownerRepository;
    private final PropertyDAO propertyDao;
    private final NotificationRepository notificationRepository;


    public PropertyService(PropertyRepository propertyRepository, OwnerRepository ownerRepository, StandardServletMultipartResolver standardServletMultipartResolver, ApplicationRepository applicationRepository, PropertyDAO propertyDao, NotificationRepository notificationRepository){
        this.ownerRepository = ownerRepository;
        this.propertyRepository = propertyRepository;
        this.standardServletMultipartResolver = standardServletMultipartResolver;
        this.applicationRepository = applicationRepository;
        this.propertyDao = propertyDao;
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public List<Property> getProperties(){
        return propertyRepository.findAllNotInProcess(Status.IN_PROCESS);
    }

    @Transactional
    public List<Property> getAllProperties(){
        return propertyRepository.findAll();
    }

    @Transactional
    public void saveProperty(Property property){
        propertyRepository.save(property);
    }
    @Transactional
    public List<Property> getPropertiesByOwner(Owner owner) {
        return propertyRepository.findPropertiesByOwner(owner);

    }

    @Transactional
    public List<Property> getPropertiesByRenter(Renter renter) {
        return propertyRepository.findPropertiesByRenter(renter);
    }

    @Transactional
    public List<Property> getAvailableProperties(){
        return propertyRepository.findAvailableProperties(Status.APPROVED);
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

    public List<Property> filterProperties(String city, Integer minPrice, Integer maxPrice, String type) {
        return propertyDao.findByFilters(city, minPrice, maxPrice, type);
    }
    public boolean applyForProperty(int propertyId, Renter renter) {
        Property property = propertyRepository.findById(propertyId).get();

        // Check if renter has already applied
        boolean alreadyApplied = applicationRepository.existsByPropertyAndRenter(property, renter);
        if (alreadyApplied) {
            return false; // Prevent duplicate applications
        }
        // Create a new application
        Application application = new Application();
        application.setProperty(property);
        application.setRenter(renter);
        application.setOwner(property.getOwner());
        application.setStartDate(LocalDate.now());
        application.setStatus(Status.IN_PROCESS); // Set status to IN_PROCESS

        //Create Notification
        Notification notification = new Notification();
        notification.setSender(renter);
        notification.setReceiver(property.getOwner());
        notification.setTask(NotificationTask.VERIFY_APPLICATION_FROM_USER);
        notificationRepository.save(notification);


        applicationRepository.save(application);
        return true;
    }


}
