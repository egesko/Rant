package tables.demo.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import tables.demo.Rant.Rant;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String fullName;
    private String email;
    private String password;
    private boolean isAdmin;

    @OneToMany
    private List<Rant> rants;


    // =============================== Constructors ================================== //


    public User(String name, String email, String password, boolean isAdmin) {
        this.fullName = name;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        rants = new ArrayList<>();
    }

    public User() {
        rants = new ArrayList<>();
    }

    // =============================== Getters and Setters for each field ================================== //

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getFullName(){
        return fullName;
    }

    public void setFullName(String fullName){
        this.fullName = fullName;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public boolean getIsAdmin(){
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin){
        this.isAdmin = isAdmin;
    }

    public List<Rant> getRants() {
        return rants;
    }

    public void setRants(List<Rant> rants) {
        this.rants = rants;
    }

    public void addRants(Rant rant){
        this.rants.add(rant);
    }


}
