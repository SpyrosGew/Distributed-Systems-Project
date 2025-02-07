package gr.hua.dit.ds.ds_lab_2024.repositories;

import gr.hua.dit.ds.ds_lab_2024.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Integer> {
    List<Application> findApplicationsByStatus(Status approvalStatus);
    boolean existsByPropertyAndRenter(Property property, Renter renter);
    List<Application> findByPropertyOwner(Owner owner);
}
