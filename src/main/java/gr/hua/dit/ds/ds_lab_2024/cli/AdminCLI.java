package gr.hua.dit.ds.ds_lab_2024.cli;
import gr.hua.dit.ds.ds_lab_2024.entities.Owner;
import gr.hua.dit.ds.ds_lab_2024.entities.Renter;
import gr.hua.dit.ds.ds_lab_2024.entities.User;
import gr.hua.dit.ds.ds_lab_2024.service.AdminService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Scanner;

@Component
public class AdminCLI implements CommandLineRunner {

    private final AdminService adminService;

    public AdminCLI(AdminService adminService){
        this.adminService = adminService;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while(!exit){

            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. Validate new user Requests");
            System.out.println("2. Approve/Decline new Properties");
            System.out.println("3. Add new user manually");
            System.out.println("4. Update a user's information");
            System.out.println("5. Delete a Renter");
            System.out.println("6. See all Renters");
            System.out.println("7. See all Owners");
            System.out.println("8. See all Users");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice){
                case 1:
                    validateNewUsers();
                    break;
                case 2:
                    approveNewProperties();
                    break;
                case 3:
                    addNewUserManually();
                    break;
                case 4:
                    updateUserInfo();
                    break;
                case 5:
                    deleteRenter();
                    break;
                case 6:
                    seeAllRenters();
                    break;
                case 7:
                    seeAllOwners();
                    break;
                case 8:
                    seeAllUsers();
                    break;
                case 9:
                    exit = true;
                    System.out.println("Exiting Admin Menu");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again");
                    break;
            }
        }
    }

    private void validateNewUsers(){

    }

    private void approveNewProperties(){

    }

    private void addNewUserManually(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Is the user a Renter or an Owner?");
        System.out.println("1)Renter 2)Owner");
        int choice = Integer.parseInt(scanner.nextLine());
        if (choice == 1){
            Renter newRenter = new Renter();
            System.out.println("Enter the email address of the new Renter");
            newRenter.setEmail(scanner.nextLine());
            System.out.println("Enter the username of the new Renter");
            newRenter.setUsername(scanner.nextLine());
            System.out.println("Enter the password of the new Renter");
            newRenter.setPassword(scanner.nextLine());
            adminService.addRenter(newRenter);
        } else if (choice == 2) {
            Owner newOwner = new Owner();
            System.out.println("Enter the email address of the new Renter");
            newOwner.setEmail(scanner.nextLine());
            System.out.println("Enter the username of the new Renter");
            newOwner.setUsername(scanner.nextLine());
            System.out.println("Enter the password of the new Renter");
            newOwner.setPassword(scanner.nextLine());
            adminService.addOwner(newOwner);
        }
    }

    private void seeAllRenters(){
        List<Renter> allRenters = adminService.getAllRenters();
        for (Renter renter : allRenters ){
            System.out.println(renter);
        }
    }

    private void seeAllOwners(){
        List<Owner> allOwners = adminService.getAllOwners();
        for(Owner owner : allOwners){
            System.out.println(owner);
        }
    }

    private void seeAllUsers(){
        List<User> allUsers = adminService.getAllUsers();
        for(User user: allUsers){
            System.out.println(user);
        }
    }

    private void updateUserInfo(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter User's ID");
        int id = Integer.parseInt(scanner.nextLine());
        User user = adminService.getUserById(id);
        System.out.println(user);

    }

    private void deleteRenter(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Renter's ID");
        int id = Integer.parseInt(scanner.nextLine());
        adminService.deleteRenterById(id);
    }

}
