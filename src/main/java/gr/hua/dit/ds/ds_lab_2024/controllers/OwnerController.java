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


}
