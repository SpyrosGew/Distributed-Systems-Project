package gr.hua.dit.ds.ds_lab_2024.controllers;


import gr.hua.dit.ds.ds_lab_2024.dao.UserDAOImpl;
import gr.hua.dit.ds.ds_lab_2024.entities.Notification;
import gr.hua.dit.ds.ds_lab_2024.entities.User;
import gr.hua.dit.ds.ds_lab_2024.dao.UserDAO;
import gr.hua.dit.ds.ds_lab_2024.repositories.NotificationRepository;
import gr.hua.dit.ds.ds_lab_2024.repositories.UserRepository;
import gr.hua.dit.ds.ds_lab_2024.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("notifications")
public class NotificationController {
    private final UserRepository userRepository;
    private final UserDAO userDAO; // For fetching notifications
    private final NotificationService notificationService;

    public NotificationController(UserRepository userRepository, UserDAO userDAO, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.userDAO = userDAO;
        this.notificationService = notificationService;
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

    @Operation(summary = "Clear users notifications", description = "Delete the users notifications by getting the users id")
    @PostMapping("/clear")
    public String clearNotifications(Authentication authentication) {
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        notificationService.deleteNotifications(currentUser.getId());
        return "notification/notifications";
    }


}