package gr.hua.dit.ds.ds_lab_2024.controllers;

import gr.hua.dit.ds.ds_lab_2024.entities.Application;
import gr.hua.dit.ds.ds_lab_2024.entities.Status;
import gr.hua.dit.ds.ds_lab_2024.entities.User;
import gr.hua.dit.ds.ds_lab_2024.repositories.ApplicationRepository;
import gr.hua.dit.ds.ds_lab_2024.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;

    public AdminController(UserRepository userRepository, ApplicationRepository applicationRepository) {
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
    }

    @Operation(summary = "Show user approvals", description = "Displays the list of users with 'IN_PROCESS' approval status waiting approval.")
    @GetMapping("/aprovals")
    public String showUserApprovals(Model model) {
        // Fetch users with 'IN_PROCESS' status (pending approval)
        List<User> pendingUsers = userRepository.findByApprovalStatus(Status.IN_PROCESS);

        model.addAttribute("pendingUsers", pendingUsers);
        return "admin/userApprovals";
    }

    @Operation(summary = "Show property applications", description = "Displays the list of applications with 'IN_PROCESS' approval status waiting approval.")
    @GetMapping("/applications")
    public String showApplications(Model model) {
        List<Application> applications = applicationRepository.findApplicationsByStatus(Status.IN_PROCESS);
        model.addAttribute("applications", applications);
        return "admin/applications";
    }

    @Operation(summary = "Show property application", description = "Displays an application")
    @GetMapping("/application/{application_id}")
    public String showApplication(@PathVariable int application_id, Model model) {
        model.addAttribute("application", applicationRepository.findById(application_id));
        return "admin/application";
    }

    @Operation(summary = "Approve Application", description = "Update application's status to APPROVED.")
    @GetMapping("/applications/{application_id}")
    public String approveApplication( @PathVariable int id, Model model) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Application with id " + id + " not found"));
        application.setStatus(Status.APPROVED);

        applicationRepository.save(application);
        model.addAttribute("sucess message", "Application Approved");

        List<Application> applications = applicationRepository.findAll();
        model.addAttribute("applications", applications);
        return "admin/applications";
    }
}
