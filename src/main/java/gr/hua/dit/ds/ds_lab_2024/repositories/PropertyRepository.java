package gr.hua.dit.ds.ds_lab_2024.repositories;

import gr.hua.dit.ds.ds_lab_2024.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Integer> {
    //List<Property> findByRenter(User renter);
    @Query("SELECT p FROM Property p WHERE p.owner = :owner")
    List<Property> findPropertiesByOwner(@Param("owner") Owner owner);

    @Query("SELECT p FROM Property p JOIN p.renters r WHERE r = :renter")
    List<Property> findPropertiesByRenter(@Param("renter") Renter renter);

    @Query("SELECT p FROM Property p WHERE p.status <> :status")
    List<Property> findAllNotInProcess(@Param("status") Status status);

}
