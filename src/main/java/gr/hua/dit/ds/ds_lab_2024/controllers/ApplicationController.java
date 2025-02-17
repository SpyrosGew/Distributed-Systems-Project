package gr.hua.dit.ds.ds_lab_2024.controllers;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import gr.hua.dit.ds.ds_lab_2024.entities.*;
import gr.hua.dit.ds.ds_lab_2024.repositories.ApplicationRepository;
import gr.hua.dit.ds.ds_lab_2024.repositories.NotificationRepository;
import gr.hua.dit.ds.ds_lab_2024.repositories.PropertyRepository;
import gr.hua.dit.ds.ds_lab_2024.repositories.UserRepository;
import gr.hua.dit.ds.ds_lab_2024.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/application")
public class ApplicationController {
    private final ApplicationRepository applicationRepository;
    private final ApplicationService applicationService;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final PropertyRepository propertyRepository;

    public ApplicationController(ApplicationRepository applicationRepository, ApplicationService applicationService, UserRepository userRepository, NotificationRepository notificationRepository, PropertyRepository propertyRepository) {
        this.applicationRepository = applicationRepository;
        this.applicationService = applicationService;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.propertyRepository = propertyRepository;
    }

    @Operation(summary = "Get Owners applications", description = "Get owners applications that have not been approved")
    @GetMapping("/applications")
    public String getOwnerApplications(Model model, Authentication authentication) {
        String username = authentication.getName();

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        List<Application> ownersApplications = applicationService.getOwnersApplications((Owner) currentUser);
        model.addAttribute("applications", ownersApplications);
        return "application/applications";
    }

    @Operation(summary = "Approve Application", description = "Update application's status to APPROVED.")
    @PostMapping("/approve/{id}")
    public String approveApplication( @PathVariable int id, Model model, Authentication authentication) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Application with id " + id + " not found"));
        application.setStatus(Status.APPROVED);

        applicationRepository.save(application);

        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        List<Application> ownersApplications = applicationService.getOwnersApplications((Owner) currentUser);
        model.addAttribute("applications", ownersApplications);
        model.addAttribute("successMessage", "Application Approved");

        // Set Property's Renter
        Property property = application.getProperty();
        property.setRenter(application.getRenter());
        propertyRepository.save(property);

        // Create Notification
        Notification notification = new Notification();
        notification.setSender(currentUser);
        notification.setReceiver(application.getRenter());
        notification.setTask(NotificationTask.VERIFY_APPLICATION_FROM_OWNER);
        notification.setPropertyId(application.getProperty().getId());

        notificationRepository.save(notification);


        return "application/applications";
    }

    @PostMapping("/decline/{id}")
    public String declineApplication(@PathVariable int id, Model model, Authentication authentication) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Application with id " + id + " not found"));
        application.setStatus(Status.DECLINED);

        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        List<Application> ownersApplications = applicationService.getOwnersApplications((Owner) currentUser);
        model.addAttribute("applications", ownersApplications);
        model.addAttribute("successMessage", "Application Declined");

        return "application/applications";
    }

}
