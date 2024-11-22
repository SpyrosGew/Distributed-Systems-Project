package gr.hua.dit.ds.ds_lab_2024.entities;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer Id;

    @Column (name = "username")
    private String username;

    @Column (name = "password")
    private String password;

    @Column (name = "email")
    private String email;


    public Owner(String name, List<Property> properties, List<Application> applications, String password, String email) {
        this.username = name;
        this.password = password;
        this.email = email;
    }

    public Owner() {
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Owner{" +
                "Id=" + Id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
