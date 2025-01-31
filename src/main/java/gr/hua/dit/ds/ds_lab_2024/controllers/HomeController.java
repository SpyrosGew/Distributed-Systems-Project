package gr.hua.dit.ds.ds_lab_2024.controllers;

import gr.hua.dit.ds.ds_lab_2024.service.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    private PropertyService propertyService;

    public HomeController(PropertyService propertyService){
        this.propertyService = propertyService;
    }
    @Operation(summary = "Home page", description = "Returns the home page of the application.")
    @GetMapping
    public String home(Model model) {
        model.addAttribute("title", "Home");
        model.addAttribute("properties", propertyService.getProperties());
        return "index"; // Main page
    }
}


