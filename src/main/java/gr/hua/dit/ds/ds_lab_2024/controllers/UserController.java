package gr.hua.dit.ds.ds_lab_2024.controllers;

import gr.hua.dit.ds.ds_lab_2024.entities.User;
import gr.hua.dit.ds.ds_lab_2024.repositories.RoleRepository;
import gr.hua.dit.ds.ds_lab_2024.service.UserService;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@Inheritance(strategy = InheritanceType.JOINED) // Can be SINGLE_TABLE, TABLE_PER_CLASS, or JOINED
public class UserController {
    private UserService userService;

    private RoleRepository roleRepository;

    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/register")
    public String register(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "auth/register";
    }
    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user, Model model){
        System.out.println("Roles: "+user.getRoles());
        Integer id = userService.saveUser(user);
        String message = "User '"+id+"' saved successfully !";
        model.addAttribute("msg", message);
        return "index";
    }

    @GetMapping("/users")
    public String showUsers(Model model){
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "auth/users";
    }

}
