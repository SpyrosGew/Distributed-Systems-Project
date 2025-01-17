package gr.hua.dit.ds.ds_lab_2024.controllers;

import gr.hua.dit.ds.ds_lab_2024.entities.Property;
import gr.hua.dit.ds.ds_lab_2024.entities.Status;
import gr.hua.dit.ds.ds_lab_2024.entities.User;
import gr.hua.dit.ds.ds_lab_2024.repositories.PropertyRepository;
import gr.hua.dit.ds.ds_lab_2024.service.PropertyService;
import org.springframework.security.access.annotation.Secured;
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

    public PropertyController(PropertyService propertyService, PropertyRepository propertyRepository) {
        this.propertyService = propertyService;
        this.propertyRepository = propertyRepository;
    }

    @Operation(summary = "Show properties", description = "Display properties")
    @RequestMapping
    public String showProperties(Model model) {
        model.addAttribute("properties", propertyService.getProperties());
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
    public String saveProperty(@ModelAttribute("course") Property property, Model model) {
        propertyService.saveProperty(property);
        model.addAttribute("courses", propertyService.getProperties());
        model.addAttribute("successMessage", "Property added successfully!");
        return "property/properties";
    }

}
