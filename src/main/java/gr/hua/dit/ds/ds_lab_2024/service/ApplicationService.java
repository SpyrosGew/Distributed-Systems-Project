package gr.hua.dit.ds.ds_lab_2024.service;

import gr.hua.dit.ds.ds_lab_2024.entities.Application;
import gr.hua.dit.ds.ds_lab_2024.entities.Owner;
import gr.hua.dit.ds.ds_lab_2024.repositories.ApplicationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationService {

    private ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository){
        this.applicationRepository = applicationRepository;
    }

    @Transactional
    public List<Application> getApplications(){
        return applicationRepository.findAll();
    }

    @Transactional
    public void saveApplication(Application application){
        applicationRepository.save(application);
    }

    @Transactional Application getApplication(Integer applicationId){
        return applicationRepository.findById(applicationId).get();
    }
    @Transactional
    public List<Application> getOwnersApplications(Owner owner) {
        return applicationRepository.findByPropertyOwner(owner);
    }
}
