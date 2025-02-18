package gr.hua.dit.ds.ds_lab_2024.entities;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import gr.hua.dit.ds.ds_lab_2024.service.PropertyService;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "notifications",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "id")
        }
)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Column(name = "task")
    @Enumerated(EnumType.ORDINAL) // Store the ordinal value of the enum
    private NotificationTask task;

    @Column(name = "status")
    private Status status;

    @Column(name = "property_id")
    private Integer propertyId;


    public Notification(User sender, User receiver, NotificationTask task, Status status) {
        this.sender = sender;
        this.receiver = receiver;
        this.task = task;
        this.status = status;
    }

    public Notification(User sender, User receiver, NotificationTask task, Status status, Integer propertyId){
        this(sender, receiver, task, status);
        this.propertyId = propertyId;

    }

    public Notification(){}



    private String getBody(){
        if(getTask() == NotificationTask.VERIFY_ACCOUNT_FROM_ADMIN){
            if(getStatus() == Status.ACCEPTED){
                return "To the most dearest new member" + receiver.getUsername() + "welcome to the app";
            } else if (getStatus() == Status.DECLINED) {
                return "Sorry your application for new account has been declined";
            }

        }else if(getTask() == NotificationTask.VERIFY_ACCOUNT_FROM_USER){
            return "To the most dearest admin of all. Please verify my account with id " + sender.getId();

        }else if(getTask() == NotificationTask.VERIFY_APPLICATION_FROM_USER){
            return "from: " + sender.getUsername()+"\n\n" + "To the most dearest owner " + receiver.getUsername() +
                    "I would like to rent one of your properties with id " + getPropertyId();

        } else if (getTask() == NotificationTask.VERIFY_APPLICATION_FROM_OWNER) {
            if (getStatus() == Status.ACCEPTED){
                return "from: " + sender.getUsername()+"\n\n"+ "To the mose dearest of renters " + receiver.getUsername() +
                        "The property rental application has been accepted see more on your applications";
            }else if (getStatus() == Status.DECLINED){
                return "from: " + sender.getUsername()+"\n\n"+ "To the mose dearest of renters " + receiver.getUsername() +
                        "The property rental application has been declined";
            }

        }else if(getTask() == NotificationTask.VERIFY_NEW_PROPERTY_FROM_OWNER){
            return "from: " + sender.getUsername() +"\n\n"+ "To the most dearest admin. Please verify my new property with id" + getPropertyId();

        } else if(getTask() == NotificationTask.VERIFY_NEW_PROPERTY_FROM_ADMIN) {
            if (getStatus() == Status.ACCEPTED) {
                return "To the most dearest owner your new property with id " + getPropertyId() + " has been accepted";
            } else if (getStatus() == Status.DECLINED) {
                return "To the most dearest owner " + receiver.getUsername() + " your new property with id " + getPropertyId() + "has been declined";
            }
        }
        return "something went wrong";
    }

    public Integer getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Integer propertyId) {
        this.propertyId = propertyId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public NotificationTask getTask() {
        return task;
    }

    public void setTask(NotificationTask task) {
        this.task = task;
    }

    public String toString(){
        return getBody();
    }
}
