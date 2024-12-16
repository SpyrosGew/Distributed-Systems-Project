package gr.hua.dit.ds.ds_lab_2024.entities;


import jakarta.persistence.*;
import gr.hua.dit.ds.ds_lab_2024.entities.Application;

import java.util.List;

@Entity
@DiscriminatorValue("OWNER")
@Table(name = "owners")
public class Owner extends User{

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Property> properties;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Application> applications;

    public Owner(String name, List<Property> properties, List<Application> applications, String password, String email) {
        super(name,email, password);
        this.properties = properties;
        this.applications = applications;
    }

    public Owner() {
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }


    public void setProperties(List<Property> properties) {
        this.properties = properties;
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
