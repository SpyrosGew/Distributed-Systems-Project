package gr.hua.dit.ds.ds_lab_2024.service;

import gr.hua.dit.ds.ds_lab_2024.entities.Owner;
import gr.hua.dit.ds.ds_lab_2024.entities.Renter;
import gr.hua.dit.ds.ds_lab_2024.entities.User;
import gr.hua.dit.ds.ds_lab_2024.repositories.OwnerRepository;
import gr.hua.dit.ds.ds_lab_2024.repositories.PropertyRepository;
import gr.hua.dit.ds.ds_lab_2024.repositories.RenterRepository;
import gr.hua.dit.ds.ds_lab_2024.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final RenterRepository renterRepository;
    private final PropertyRepository propertyRepository;
    private final OwnerRepository ownerRepository;
    private final UserRepository userRepository;

    public AdminService(RenterRepository renterRepository, PropertyRepository propertyRepository, OwnerRepository ownerRepository, UserRepository userRepository){
        this.renterRepository = renterRepository;
        this.propertyRepository = propertyRepository;
        this.ownerRepository = ownerRepository;
        this.userRepository = userRepository;
    }

    public void deleteRenterById(Integer id){
        if (renterRepository.existsById(id)){
            renterRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("User not found.");
        }
    }

    public Renter getRenterById(Integer id){
        return renterRepository.findById(id).get();
    }

    public List<Renter> getAllRenters(){
        return renterRepository.findAll();
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Integer id){
        return userRepository.findById(id).get();
    }

    public List<Owner> getAllOwners(){
        return ownerRepository.findAll();
    }

    public void addRenter(Renter renter){
        renterRepository.save(renter);
    }

    public void addOwner(Owner owner){
        ownerRepository.save(owner);
    }
}