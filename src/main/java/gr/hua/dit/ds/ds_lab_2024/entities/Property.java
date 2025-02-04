package gr.hua.dit.ds.ds_lab_2024.entities;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "address")
    private String address;  //the address of the property

    @Column(name = "city")
    private String city;  //the city of the property

    @Column(name = "square_meters")
    private int squareMeters;   //how many square meters the property is

    @Column(name = "is_available")
    private transient boolean isAvailable;   //is the property currently available (if activeApplication == NULL then this is true)

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "owner_id") // This creates the foreign key column "owner_id" in the Property table
    private Owner owner;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "property_renter",
            joinColumns = @JoinColumn(name = "property_id"),
            inverseJoinColumns = @JoinColumn(name = "renter_id")
    )
    private List<Renter> renters;  // Renters associated with the property


    @Column(name = "status")
    private Status status = Status.IN_PROCESS;

    /* transient variable is not saved in the db, i use it to determine in real time which is the active
    * application based on the current datetime and the datetime on the application, will make functionality later*/
    private transient Application activeApplication;

    private transient Renter activeRenter;

    @Column(name = "price")
    private Integer price;

    @Column(name = "type")
    private String type; // Example values: "apartment", "house"

    public Property() {
    }

    public Property(String name, int squareMeters, Owner owner, String city, int price, String type) {
        this.address = name;
        this.squareMeters = squareMeters;
        this.owner = owner;
        this.city = city;
        this.price = price;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSquareMeters() {
        return squareMeters;
    }

    public void setSquareMeters(int squareMeters) {
        this.squareMeters = squareMeters;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Application getActiveApplication() {
        return activeApplication;
    }

    public void setActiveApplication(Application activeApplication) {
        this.activeApplication = activeApplication;
    }

    public Renter getActiveRenter() {
        return activeRenter;
    }

    public void setActiveRenter(Renter activeRenter) {
        this.activeRenter = activeRenter;
    }

    @Override
    public String toString() {
        return "Property{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", squareMeters=" + squareMeters +
                ", isAvailable=" + isAvailable +
                ", owner=" + owner +
                ", activeApplication=" + activeApplication +
                ", activeRenter=" + activeRenter +
                '}';
    }
}
