package gr.hua.dit.ds.ds_lab_2024.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
/*
*  renter owner */
@Entity
@Table
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "price")
    private double price;

    @Column(name = "status")
    private Status status = Status.IN_PROCESS;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "property_id") // This creates the foreign key column "owner_id" in the Property table
    private Property property;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "renter_id") // This creates the foreign key column "owner_id" in the Property table
    private Renter renter;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "owner_id") // This creates the foreign key column "owner_id" in the Property table
    private Owner owner;

    public Application(LocalDate startDate, double price, Property property, Owner owner, Renter renter) {
        this.startDate = startDate;
        this.price = price;
        this.property = property;
        this.owner = owner;
        this.renter = renter;
    }

    public Application() {
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Renter getRenter() {
        return renter;
    }

    public void setRenter(Renter renter) {
        this.renter = renter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", price=" + price +
                ", status=" + status +
                ", property=" + property +
                ", renter=" + renter +
                ", owner=" + owner +
                '}';
    }
}
