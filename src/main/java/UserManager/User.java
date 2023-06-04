package UserManager;
import Utils.Role;

import java.util.Date;

abstract public class User {
    private String username, id, firstName, lastName, phone, email, gender, userCategory, address;
    private Date dateOfBirth;
    private int role, score;

    public User() {
        this.userCategory = "";
        this.username = "";
        this.id = "";
        this.firstName = "";
        this.lastName = "";
        this.dateOfBirth = new Date();
        this.phone = "";
        this.email = "";
        this.address = "";
        this.role = Role.UNKNOWN.getValue();
        this.score = 0;
    }
    public String getGender() {
        return this.gender;
    }
    public User(String username, String id, String firstName, String lastName, Date dateOfBirth, String phone, String email, String gender, String address, int score, String userCategory) {
        this.address = address;
        this.username = username;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.email = email;
        this.score = score;
        this.gender = gender;
        this.userCategory = userCategory;
    }
    public String getAddress() {
        return this.address;
    }
    public String getUserCategory() {
        return this.userCategory;
    }
    public String getId() {
        return this.id;
    }
    public String getUsername() {
        return this.username;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getScore() {
        return score;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
