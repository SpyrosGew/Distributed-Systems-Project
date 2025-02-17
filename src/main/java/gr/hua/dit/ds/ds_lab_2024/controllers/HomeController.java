package gr.hua.dit.ds.ds_lab_2024.controllers;

import gr.hua.dit.ds.ds_lab_2024.entities.*;
import gr.hua.dit.ds.ds_lab_2024.repositories.UserRepository;
import gr.hua.dit.ds.ds_lab_2024.service.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;

import java.util.List;


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
        model.addAttribute("title", "Home");
        model.addAttribute("properties", propertyService.getAvailableProperties());
        return "index"; // Main page
    }

    @GetMapping("/waiting-approval")
    public String showWaitingApprovalPage(Authentication authentication, Model model) {
        // Check if user is authenticated
        if (authentication == null) {
            return "redirect:/login"; // Redirect to login if not authenticated
        }

        String username = authentication.getName(); // Get the logged-in user's username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        // Check if the user is approved
        if (user.getApprovalStatus() == Status.APPROVED) {
            return "redirect:/home"; // Redirect to the main page if the user is approved
        }

        // Return the waiting approval page if not approved
        return "waitingApproval"; // Thymeleaf template to show the waiting approval page
    }

    // This method will make the user status available to all views, including the header.
    @ModelAttribute("userStatus")
    public String getUserStatus(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            return user.getApprovalStatus().toString();
        }
        return null; // Return null or default status if no user is authenticated
    }

}


