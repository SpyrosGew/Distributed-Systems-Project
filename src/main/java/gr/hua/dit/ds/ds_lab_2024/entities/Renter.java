package gr.hua.dit.ds.ds_lab_2024.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@DiscriminatorValue("RENTER")
@Table(name = "renters")
public class Renter extends User{




    public Renter(String name, String email, String password){
        super(name, email,password);
    }

    public Renter() {
    }

    @Override
    public String toString() {
        return "Renter{" +
                "Id=" + super.getId() +
                ", username='" + super.getUsername() + '\'' +
                ", password='" + super.getPassword() + '\'' +
                ", email='" + super.getEmail() + '\'' +
                '}';
    }
}
