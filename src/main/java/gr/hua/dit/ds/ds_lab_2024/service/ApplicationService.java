package gr.hua.dit.ds.ds_lab_2024.service;

import gr.hua.dit.ds.ds_lab_2024.entities.Application;
import gr.hua.dit.ds.ds_lab_2024.entities.Owner;
import gr.hua.dit.ds.ds_lab_2024.entities.Renter;
import gr.hua.dit.ds.ds_lab_2024.entities.Status;
import gr.hua.dit.ds.ds_lab_2024.repositories.ApplicationRepository;
import gr.hua.dit.ds.ds_lab_2024.repositories.NotificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    private final NotificationRepository notificationRepository;
    private ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository, NotificationRepository notificationRepository){
        this.applicationRepository = applicationRepository;
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public List<Application> getApplications(){
        return applicationRepository.findAll();
    }

    @Transactional
    public void saveApplication(Application application){
        applicationRepository.save(application);
    }

    @Transactional
    public Application getApplication(Integer applicationId){
        return applicationRepository.findById(applicationId).orElse(null);
    }
    @Transactional
    public List<Application> getOwnersApplications(Owner owner) { // Gets owners applications that have not been approved
        return applicationRepository.findByPropertyOwner(owner)
                .stream()
                .filter(app -> app.getStatus() == Status.IN_PROCESS)
                .collect(Collectors.toList());
    }
    @Transactional
    public List<Application> getRentersApplications(Renter renter) {
        return applicationRepository.findByRenter(renter);
    }
}
