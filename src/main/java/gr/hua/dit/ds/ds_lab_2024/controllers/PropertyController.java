package gr.hua.dit.ds.ds_lab_2024.controllers;

import gr.hua.dit.ds.ds_lab_2024.entities.*;
import gr.hua.dit.ds.ds_lab_2024.repositories.PropertyRepository;
import gr.hua.dit.ds.ds_lab_2024.repositories.UserRepository;
import gr.hua.dit.ds.ds_lab_2024.service.PropertyService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;


@Controller
@RequestMapping("/property")
public class PropertyController {
    private final PropertyRepository propertyRepository;
    private PropertyService propertyService;
    private UserRepository userRepository;

    public PropertyController(PropertyService propertyService, PropertyRepository propertyRepository, UserRepository userRepository) {
        this.propertyService = propertyService;
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
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

        System.out.println(properties);
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

    @Operation(summary = "Show property", description = "Display property")
    @GetMapping("property/{property_id}")
    public String showProperty(Model model, @PathVariable("property_id") int property_id) {
        Property property = propertyService.getProperty(property_id);
        model.addAttribute("property", property);
        return "property/property";
   }

    @Secured("ROLE_ADMIN")
    @Operation(summary = "Approve property", description = "Updates the approval status of a property with the specified ID to 'APPROVED'.")
    @PostMapping("/property/{id}")
    public String approveProperty(@PathVariable int property_id, Model model) {

        Property property = propertyRepository.findById(property_id)
                .orElseThrow(() -> new IllegalStateException("Property not found"));
        // Update the user's approval status
        property.setStatus(Status.APPROVED);
        propertyRepository.save(property);

        model.addAttribute("successMessage", "User approved successfully!");

        List<Property> properties = propertyRepository.findAll();
        model.addAttribute("properties", properties);

        return "/property";
    }

    @Operation(summary = "Show all properties", description = "Displays a list of all properties stored in the system.")
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
        model.addAttribute("properties", propertyService.getProperties());
        model.addAttribute("successMessage", "Property added successfully!");
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
        List<Property> properties = propertyRepository.findAll();
        model.addAttribute("properties", properties);
        return "index";
    }

}
