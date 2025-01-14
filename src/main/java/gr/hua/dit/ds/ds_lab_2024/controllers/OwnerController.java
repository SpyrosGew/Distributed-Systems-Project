package gr.hua.dit.ds.ds_lab_2024.controllers;


import gr.hua.dit.ds.ds_lab_2024.entities.Property;
import gr.hua.dit.ds.ds_lab_2024.service.OwnerService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/owner")
public class OwnerController {

    private OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }


////    public String showOwnerProperties(Model model) {}

    @Operation(summary = "Add new property", description = "Displays the form to add a new property to the system.")
    @GetMapping("/new")
    public String addProperty(Model model) {
        Property property = new Property();
        model.addAttribute("property", property);
        return "Owner/addProperty";
    }
}
