package gr.hua.dit.ds.ds_lab_2024.controllers;

import gr.hua.dit.ds.ds_lab_2024.entities.*;
import gr.hua.dit.ds.ds_lab_2024.repositories.ApplicationRepository;
import gr.hua.dit.ds.ds_lab_2024.repositories.NotificationRepository;
import gr.hua.dit.ds.ds_lab_2024.repositories.PropertyRepository;
import gr.hua.dit.ds.ds_lab_2024.repositories.UserRepository;
import gr.hua.dit.ds.ds_lab_2024.service.PropertyService;
import gr.hua.dit.ds.ds_lab_2024.service.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/property")
public class PropertyController {
    private final PropertyRepository propertyRepository;
    private final UserService userService;
    private PropertyService propertyService;
    private UserRepository userRepository;
    private NotificationRepository notificationRepository;
    private ApplicationRepository applicationRepository;
    public PropertyController(PropertyService propertyService, PropertyRepository propertyRepository, UserRepository userRepository, NotificationRepository notificationRepository, ApplicationRepository applicationRepository, UserService userService) {
        this.propertyService = propertyService;
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.applicationRepository = applicationRepository;
        this.userService = userService;
    }

    @Operation(summary = "Show properties", description = "Display properties")
    @RequestMapping
    public String showUsersProperties(Model model, Authentication authentication) {
        // Get the username from the authenticated user
        String username = authentication.getName(); // Should now return the username

        // Fetch the user from the database
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        boolean isOwner = currentUser.getRoles().stream()
                .map(Role::getName)
                .anyMatch(role -> role.equals("ROLE_OWNER"));

        boolean isRenter = currentUser.getRoles().stream()
                .map(Role::getName)
                .anyMatch(role -> role.equals("ROLE_RENTER"));

        List<Property> properties = propertyService.getProperties();
        if(isOwner) {
            properties = propertyService.getPropertiesByOwner((Owner) currentUser);
        } else if(isRenter) {
            properties = propertyService.getPropertiesByRenter((Renter) currentUser);
        }

        model.addAttribute("properties", properties);
        return "property/properties";
    }

    @Operation(summary = "Get filtered properties", description = "Display the properties based on the filters")
    @GetMapping("/filteredproperties")
    public String filteredProperties(Model model, @RequestParam(required = false) String city, @RequestParam(required = false) Integer minPrice, @RequestParam(required = false) Integer maxPrice, @RequestParam(required = false) String type) {
        System.out.println("City: " + city + ", MinPrice: " + minPrice + ", MaxPrice: " + maxPrice + ", Type: " + type);

        List<Property> filteredProperties = propertyService.filterProperties(city, minPrice, maxPrice, type);
        System.out.println(filteredProperties);

        model.addAttribute("properties", filteredProperties);
        return "index";
    }

    @GetMapping("/properties")
    public String getAllProperties(Model model){
        List<Property> allProperties = propertyService.getAllProperties();
        model.addAttribute("properties", allProperties);
        return "property/properties";
    }

    @Operation(summary = "Show property", description = "Display property")
    @GetMapping("property/{property_id}")
    public String showProperty(Model model, @PathVariable("property_id") int property_id) {
        Property property = propertyService.getProperty(property_id);
        model.addAttribute("property", property);
        return "property/property";
   }

    @Secured("ROLE_ADMIN")
    @Operation(summary = "Approve property", description = "Updates the approval status of a property with the specified ID to 'APPROVED'.")
    @PostMapping("/approve/{id}")
    public String approveProperty(@PathVariable("id") int property_id, Model model, Authentication authentication) {
        String username = authentication.getName(); // Should now return the username

        // Fetch the user from the database
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        Property property = propertyRepository.findById(property_id)
                .orElseThrow(() -> new IllegalStateException("Property not found"));
        // Update the user's approval status
        property.setStatus(Status.APPROVED);
        propertyRepository.save(property);

        model.addAttribute("successMessage", "Property approved successfully!");

        Notification notification = new Notification();
        notification.setTask(NotificationTask.VERIFY_NEW_PROPERTY_FROM_ADMIN);
        notification.setPropertyId(property_id);
        notification.setSender(currentUser); // Admin
        notification.setReceiver(property.getOwner());
        notification.setStatus(Status.ACCEPTED);
        notificationRepository.save(notification);


        List<Property> properties = propertyRepository.findAll();
        model.addAttribute("properties", properties);

        return "/property/properties";
    }

    @Operation(summary = "Show property form", description = "Displays the form where an owner can add a property.")
    @GetMapping("/new")
    public String addProperty(Model model) {
        Property property = new Property();
        model.addAttribute("property", property);
        return "property/property_form";}

    @Operation(summary = "Save new property", description = "Saves a newly created property to the system and displays the updated property list.")
    @PostMapping("/new")
    public String saveProperty(@ModelAttribute("course") Property property, Model model, Authentication authentication) {
        // Get the username from the authenticated user
        String username = authentication.getName(); // Should now return the username

        // Fetch the user from the database
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        property.setOwner((Owner) currentUser);
        propertyService.saveProperty(property);
        model.addAttribute("properties", propertyService.getPropertiesByOwner((Owner) currentUser));
        model.addAttribute("successMessage", "Property added successfully! Waiting for admin approval...");

        Notification notification = new Notification();
        notification.setTask(NotificationTask.VERIFY_NEW_PROPERTY_FROM_OWNER);
        notification.setPropertyId(property.getId());
        notification.setSender(currentUser);
        notification.setReceiver(userService.findUserByRole("ROLE_ADMIN"));
        notificationRepository.save(notification);


        return "property/properties";
    }

    @Operation(summary = "Renter apply for property", description = "Renter can i apply for to rent the property")
    @GetMapping("/apply/{id}")
    public String applyForProperty(@PathVariable("id") int propertyId, Authentication authentication, Model model) {
        String username = authentication.getName();

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        boolean success = propertyService.applyForProperty(propertyId, (Renter) currentUser);
        if (success) {
            model.addAttribute("success", "Property approved successfully!");
        } else {
            model.addAttribute("error", "Property could not be approved!");
        }
        List<Property> properties = propertyService.getAvailableProperties();
        model.addAttribute("properties", properties);
        return "index";
    }

    @Operation(summary = "Delete Property", description = "Delete property. Admin and Owner can do this")
    @PostMapping("/remove/{id}")
    public String removeProperty(@PathVariable("id") int propertyId, Authentication authentication, Model model) {
        Property property = propertyService.getProperty(propertyId);

        // Remove references to this property in the application table
        List<Application> applications = applicationRepository.findByProperty(property);
        applicationRepository.deleteAll(applications);

        propertyRepository.delete(property);
        model.addAttribute("successMessage", "Property deleted successfully!");

        String username = authentication.getName();

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        boolean isOwner = currentUser.getRoles().stream()
                .map(Role::getName)
                .anyMatch(role -> role.equals("ROLE_OWNER"));

        boolean isAdmin = currentUser.getRoles().stream()
                .map(Role::getName)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));


        List<Property> properties;
        if(isOwner) {
            properties = propertyService.getPropertiesByOwner((Owner) currentUser);
            model.addAttribute("properties", properties);
            model.addAttribute("successMessage", "Property deleted successfully!");
            return "/property/properties";
        } else if(isAdmin) {
            properties = propertyRepository.findAll();
            model.addAttribute("properties", properties);
            model.addAttribute("successMessage", "Property deleted successfully!");
            return "/property/properties";
        }
        model.addAttribute("successMessage", "Property could not be removed!");
        return "/property/properties";
    }

    @Secured("ROLE_OWNER")
    @Operation(summary = "Remove renter from property", description = "Remove the renter of the property")
    @PostMapping("/removeRenter/{id}")
    public String removeRenter(@PathVariable("id") int propertyId, Authentication authentication, Model model) {
        Property property = propertyService.getProperty(propertyId);
        property.setRenter(null);

        String username = authentication.getName(); // Should now return the username

        // Fetch the user from the database
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        List<Property> properties = propertyService.getPropertiesByOwner((Owner) currentUser);

        model.addAttribute("properties", properties);
        model.addAttribute("successMessage", "Renter removed successfully!");
        return "property/properties";

    }

}
