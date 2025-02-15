package gr.hua.dit.ds.ds_lab_2024.controllers;

import gr.hua.dit.ds.ds_lab_2024.entities.User;
import gr.hua.dit.ds.ds_lab_2024.repositories.UserRepository;
import gr.hua.dit.ds.ds_lab_2024.service.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;



@Controller
@RequestMapping("/")
public class HomeController {
    private PropertyService propertyService;
    private UserRepository userRepository;

    public HomeController(PropertyService propertyService, UserRepository userRepository) {
        this.propertyService = propertyService;
        this.userRepository = userRepository;
    }
    @Operation(summary = "Home page", description = "Returns the home page of the application.")
    @GetMapping
    public String home(Model model, Authentication authentication) {
        String username = authentication.getName();

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        model.addAttribute("userStatus", currentUser.getApprovalStatus());
        System.out.println(currentUser.getApprovalStatus().toString());
        model.addAttribute("title", "Home");
        model.addAttribute("properties", propertyService.getProperties());
        return "index"; // Main page
    }
}


