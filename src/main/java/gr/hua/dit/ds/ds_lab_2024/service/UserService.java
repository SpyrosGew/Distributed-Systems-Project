package gr.hua.dit.ds.ds_lab_2024.service;

import gr.hua.dit.ds.ds_lab_2024.dao.UserDAO;
import gr.hua.dit.ds.ds_lab_2024.dao.UserDAOImpl;
import gr.hua.dit.ds.ds_lab_2024.entities.*;
import gr.hua.dit.ds.ds_lab_2024.repositories.OwnerRepository;
import gr.hua.dit.ds.ds_lab_2024.repositories.RenterRepository;
import gr.hua.dit.ds.ds_lab_2024.repositories.RoleRepository;
import gr.hua.dit.ds.ds_lab_2024.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final OwnerRepository ownerRepository;
    private RenterRepository renterRepository;
    private UserDAOImpl userDAO;
    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder, OwnerRepository ownerRepository, RenterRepository renterRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.ownerRepository = ownerRepository;
        this.renterRepository = renterRepository;
    }


    public Optional<User> authenticate(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty() || !passwordEncoder.matches(password, userOptional.get().getPassword())) {
            throw new UsernameNotFoundException("Invalid username or password");
        }

        return userOptional;
    }
    @Transactional
    public Integer saveUser(User user) {
        String passwd= user.getPassword();
        String encodedPassword = passwordEncoder.encode(passwd);
        user.setPassword(encodedPassword);

        Role role = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        user = userRepository.save(user);
        return user.getId();
    }

    @Transactional
    public Integer updateUser(User user) {
        user = userRepository.save(user);
        return user.getId();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> opt = userRepository.findByUsername(username);

        if(opt.isEmpty())
            throw new UsernameNotFoundException("User with email: " +username +" not found !");
        else {
            User user = opt.get();
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    user.getRoles()
                            .stream()
                            .map(role-> new SimpleGrantedAuthority(role.toString()))
                            .collect(Collectors.toSet())
            );
        }
    }

    @Transactional
    public Object getUsers() {
        return userRepository.findAll();
    }

    public User getUser(Integer userId) {
        return userRepository.findUserById(userId);
    }

    @Transactional
    public void updateOrInsertRole(Role role) {
        roleRepository.updateOrInsert(role);
    }


    @Transactional
    public void registerRenter(Renter renter) {
        String passwd= renter.getPassword();
        String encodedPassword = passwordEncoder.encode(passwd);
        renter.setPassword(encodedPassword);

        Role role = roleRepository.findByName("ROLE_RENTER")
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        renter.setRoles(roles);
        renter.setApprovalStatus(Status.IN_PROCESS);

        userRepository.save(renter);  // Save the User (base class)
        renterRepository.save(renter); // Save the Renter-specific data
    }

    public void registerOwner(Owner owner) {
        String passwd= owner.getPassword();
        String encodedPassword = passwordEncoder.encode(passwd);
        owner.setPassword(encodedPassword);

        Role role = roleRepository.findByName("ROLE_OWNER")
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        owner.setRoles(roles);
        owner.setApprovalStatus(Status.IN_PROCESS);

        userRepository.save(owner);  // Save the User (base class)
        ownerRepository.save(owner); // Save the Renter-specific data
    }

    @Transactional
    public List<Notification> getUsersNotification(Integer id){
        return userDAO.getUserNotifications(id);
    }

    @Transactional
    public User findUserByRole(String Role){
        return userRepository.findByRoleName(Role);
    }



}