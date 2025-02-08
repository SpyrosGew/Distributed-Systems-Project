package gr.hua.dit.ds.ds_lab_2024.controllers;


import gr.hua.dit.ds.ds_lab_2024.entities.Application;
import gr.hua.dit.ds.ds_lab_2024.entities.Owner;
import gr.hua.dit.ds.ds_lab_2024.entities.Property;
import gr.hua.dit.ds.ds_lab_2024.entities.User;
import gr.hua.dit.ds.ds_lab_2024.repositories.UserRepository;
import gr.hua.dit.ds.ds_lab_2024.service.ApplicationService;
import gr.hua.dit.ds.ds_lab_2024.service.OwnerService;
import gr.hua.dit.ds.ds_lab_2024.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;


import java.util.List;

@Controller
@RequestMapping("/owner")
public class OwnerController {


    private OwnerService ownerService;
    private ApplicationService applicationService;
    private UserRepository userRepository;
    public OwnerController(OwnerService ownerService, ApplicationService applicationService, UserRepository userRepository) {
        this.ownerService = ownerService;
        this.applicationService = applicationService;
        this.userRepository = userRepository;
    }

    @GetMapping("applications")
    public String getOwnerApplications(Model model, Authentication authentication) {
        String username = authentication.getName();

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        List<Application> ownersApplications = applicationService.getOwnersApplications((Owner) currentUser);
        model.addAttribute("ownersApplications", ownersApplications);
        return "owner/ownerApplications";
    }
}
