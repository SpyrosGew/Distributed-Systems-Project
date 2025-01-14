package gr.hua.dit.ds.ds_lab_2024.controllers;


import gr.hua.dit.ds.ds_lab_2024.dao.UserDAOImpl;
import gr.hua.dit.ds.ds_lab_2024.entities.Notification;
import gr.hua.dit.ds.ds_lab_2024.entities.User;
import gr.hua.dit.ds.ds_lab_2024.dao.UserDAO;
import gr.hua.dit.ds.ds_lab_2024.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("notifications")
public class NotificationController {
    private final UserRepository userRepository;
    private final UserDAO userDAO; // For fetching notifications

    public NotificationController(UserRepository userRepository, UserDAO userDAO) {
        this.userRepository = userRepository;
        this.userDAO = userDAO;
    }
    @Operation(summary = "Get user notifications", description = "Fetches the notifications for the authenticated user based on their username.")
    @GetMapping()
    public String getUserNotifications(Authentication authentication, Model model) {
        // Get the username from the authenticated user
        String username = authentication.getName(); // Should now return the username

        // Fetch the user from the database
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        System.out.println("Authenticated username: " + username);

        // Retrieve notifications
        List<Notification> notifications = userDAO.getUserNotifications(currentUser.getId());
        model.addAttribute("notifications", notifications);

        return "notification/notifications";
    }

}