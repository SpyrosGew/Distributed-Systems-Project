package gr.hua.dit.ds.ds_lab_2024.entities;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table
public class Owner extends User{


    public Owner(String name, List<Property> properties, List<Application> applications, String password, String email) {
        super(name,password,email);
    }

    public Owner() {
    }


    @Override
    public String toString() {
        return "Owner{" +
                "Id=" + super.getId() +
                ", username='" + super.getUsername() + '\'' +
                ", password='" + super.getPassword() + '\'' +
                ", email='" + super.getEmail() + '\'' +
                '}';
    }
}
