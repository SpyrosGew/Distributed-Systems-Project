package gr.hua.dit.ds.ds_lab_2024.controllers;

import gr.hua.dit.ds.ds_lab_2024.entities.Owner;
import gr.hua.dit.ds.ds_lab_2024.entities.Renter;
import gr.hua.dit.ds.ds_lab_2024.entities.User;
import gr.hua.dit.ds.ds_lab_2024.repositories.RoleRepository;
import gr.hua.dit.ds.ds_lab_2024.repositories.UserRepository;
import gr.hua.dit.ds.ds_lab_2024.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



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

    @GetMapping("/register")
    public String register(Model model) {
        User user = new User();
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("user", user);
        return "auth/register";
    }


        @PostMapping("/saveUser")
        public String saveUser(@ModelAttribute User user, @RequestParam("role") String role, Model model){
            System.out.println("Roles: "+user.getRoles());
            // Check for existing user
            if (userRepository.existsByUsername(user.getUsername()) || userRepository.existsByEmail(user.getEmail())) {
                model.addAttribute("error", "User with the same username or email already exists.");
                model.addAttribute("user", user);
                model.addAttribute("roles", roleRepository.findAll());
                return "auth/register";
            }
            // Determine role and create specific user type
            if ("ROLE_OWNER".equals(role)) {
                Owner owner = new Owner(user.getUsername(), user.getEmail(), user.getPassword(), null, null);
                userService.registerOwner(owner);
            } else if ("ROLE_RENTER".equals(role)) {
                Renter renter =new Renter(user.getUsername(), user.getEmail(), user.getPassword());
                userService.registerRenter(renter);
            } else {
                model.addAttribute("error", "Invalid role selected!");
                return "auth/register";
            }
            String message = "Registration successful! Waiting for approval from admin...";
            model.addAttribute("msg", message);
        return  "index";
    }

    @GetMapping("/users")
    public String showUsers(Model model){
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "auth/users";
    }

}
