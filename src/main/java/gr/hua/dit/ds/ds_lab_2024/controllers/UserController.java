package gr.hua.dit.ds.ds_lab_2024.controllers;

import gr.hua.dit.ds.ds_lab_2024.entities.*;
import gr.hua.dit.ds.ds_lab_2024.repositories.RoleRepository;
import gr.hua.dit.ds.ds_lab_2024.repositories.UserRepository;
import gr.hua.dit.ds.ds_lab_2024.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
* Access API Documentation
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
            User admin = new User(user.getUsername(), user.getEmail(), user.getPassword(), Status.APPROVED);
            userService.saveUser(admin);
        } else {
            model.addAttribute("error", "Invalid role selected!");
            return "auth/register";
        }
        String message = "Registration successful! Waiting for approval from admin...";
        model.addAttribute("msg", message);
        return  "index";
    }

    @Operation(summary = "View all users", description = "Displays a list of all registered users. Requires admin privileges.")
    @Secured("ROLE_ADMIN")
    @GetMapping("/users")
    public String showUsers(Model model){
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "auth/users";
    }

    @Operation(summary = "View User", description = "Displays a register user. Requires admin privileges.")
    @Secured("ROLE_ADMIN")
    @GetMapping("/user/{user_id}")
    public String showUser(@PathVariable int user_id, Model model){
        model.addAttribute("user", userService.getUser(user_id));
        return "auth/user";
    }

    @Secured("ROLE_ADMIN")
    @Operation(summary = "Update User", description = "Admin update's the user's information.")
    @PostMapping("/user/{user_id}")
    public String updateUser(@PathVariable int user_id, @RequestParam("role") String role, @ModelAttribute("user") User user, Model model){
        User the_user = (User) userService.getUser(user_id);
        the_user.setEmail(user.getEmail());
        the_user.setUsername(user.getUsername());
        userService.updateUser(the_user);
        model.addAttribute("users", userService.getUsers());
        return "auth/users";


    }

    @Operation(summary = "Approve a user", description = "Updates the approval status of a user with the specified ID to 'APPROVED'.")
    @Secured("ROLE_ADMIN")
    @PostMapping("/users/{id}")
    public String approveUser(@PathVariable int id, Model model) {
        // Fetch the user by ID
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        // Update the user's approval status
        user.setApprovalStatus(Status.APPROVED);
        userRepository.save(user);

        model.addAttribute("successMessage", "User approved successfully!");

        List<User> pendingUsers = userRepository.findAll();
        model.addAttribute("pendingUsers", pendingUsers);
        return "admin/userApprovals";
    }

    @Operation(summary = "Add a role to a user", description = "Assigns a specific role to a user by their user ID and role ID. The updated user information is then displayed.")
    @GetMapping("/user/role/add/{user_id}/{role_id}")
    public String addRoletoUser(@PathVariable int user_id, @PathVariable Integer role_id, Model model){
        User user = (User) userService.getUser(user_id);
        Role role = roleRepository.findById(role_id).get();
        user.getRoles().add(role);
        System.out.println("Roles: "+user.getRoles());
        userService.updateUser(user);
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "auth/users";

    }


    @Operation(summary = "Delete a role from a user", description = "Deletes a specific role from a user by their user ID and role ID. The updated user information is then displayed.")
    @GetMapping("/user/role/delete/{user_id}/{role_id}")
    public String deleteRolefromUser(@PathVariable int user_id, @PathVariable Integer role_id, Model model){
        User user = (User) userService.getUser(user_id);
        Role role = roleRepository.findById(role_id).get();
        user.getRoles().remove(role);
        System.out.println("Roles: "+user.getRoles());
        userService.updateUser(user);
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "auth/users";

    }
}
 