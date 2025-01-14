package gr.hua.dit.ds.ds_lab_2024.controllers;

import gr.hua.dit.ds.ds_lab_2024.entities.Owner;
import gr.hua.dit.ds.ds_lab_2024.entities.Renter;
import gr.hua.dit.ds.ds_lab_2024.entities.Status;
import gr.hua.dit.ds.ds_lab_2024.entities.User;
import gr.hua.dit.ds.ds_lab_2024.repositories.RoleRepository;
import gr.hua.dit.ds.ds_lab_2024.repositories.UserRepository;
import gr.hua.dit.ds.ds_lab_2024.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/*
* Access OpenAPI Documentation
*
* http://localhost:8080/swagger-ui.html
*
*
* */


@Controller
public class UserController {
    private UserService userService;

    private RoleRepository roleRepository;

    private UserRepository userRepository;

    public UserController(UserService userService, RoleRepository roleRepository, UserRepository userRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;

    }

    @Operation(summary = "Register a new user", description = "Registers a user with the selected role")
    @GetMapping("/register")
    public String register(Model model) {
        User user = new User();
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("user", user);
        return "auth/register";
    }

    @Operation(summary = "Save a new user", description = "Saves the user after registration and assigns the appropriate role")
    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user, @RequestParam("role") String role, Model model) {
        System.out.println("Roles: " + user.getRoles());
        // Check for existing user
        if (userRepository.existsByUsername(user.getUsername()) || userRepository.existsByEmail(user.getEmail())) {
               model.addAttribute("error", "User with the same username or email already exists.");
               model.addAttribute("user", user);
               model.addAttribute("roles", roleRepository.findAll());
               return "auth/register";
        }
        // Determine role and create specific user type
        if ("ROLE_OWNER".equals(role)) {Owner owner = new Owner(user.getUsername(), user.getEmail(), user.getPassword(), null, null);
            userService.registerOwner(owner);
        } else if ("ROLE_RENTER".equals(role)) {
            Renter renter = new Renter(user.getUsername(), user.getEmail(), user.getPassword());
            userService.registerRenter(renter);
        } else if ("ROLE_ADMIN".equals(role)) {
            User admin = new User(user.getUsername(), user.getEmail(), user.getPassword(), Status.IN_PROCESS);
            userService.saveUser(admin);
        } else {
            model.addAttribute("error", "Invalid role selected!");
            return "auth/register";
        }
        String message = "Registration successful! Waiting for approval from admin...";
        model.addAttribute("msg", message);return  "index";
    }


    @Operation(summary = "View all users", description = "Displays a list of all registered users. Requires admin privileges.")
    @Secured("ROLE_ADMIN")
    @GetMapping("/users")
    public String showUsers(Model model){
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "auth/users";
    }

}
 