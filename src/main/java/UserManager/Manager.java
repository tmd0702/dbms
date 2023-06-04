package UserManager;
import Utils.Role;

import java.util.Date;

public class Manager extends User{
    public Manager() {
        super();
        setRole(Role.MANAGER.getValue());
    }
    public Manager(String username, String id, String firstName, String lastName, Date dateOfBirth, String phone, String email, String gender, String address, String userCategory) {
        super(username, id, firstName, lastName, dateOfBirth, phone, email, gender, address, 1899, userCategory);
        setRole(Role.MANAGER.getValue());
    }

}
