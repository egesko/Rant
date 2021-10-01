package tables.demo.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import tables.demo.Group.Group;
import tables.demo.Rant.Rant;

@Entity
@JsonIgnoreProperties(value = {"email", "password", "rants", "isAdmin", "likedTags"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String fullName;
    private String email;
    private String password;
    private boolean isAdmin;
    private boolean wantsProfanity;

    // List of rant tags that user likes
    @ElementCollection
    private List<String> likedTags;

    // List of rants tied to user
    @OneToMany
    private List<Rant> rants;

    // List of Groups the user is a Owner of
    @OneToMany
    @JsonIgnore
    private List<Group> groupOwners;

    // List of Groups the user is a Moderator of
    @ManyToMany
    @JsonIgnore
    private List<Group> groupModerators;

    // List of Groups the user is a Member of
    @ManyToMany
    @JsonIgnore
    private List<Group> groupMembers;

    // List of rants user has seen in this session. (To be reset every session)
    @OneToMany
    private List<Rant> seenRants;



    // =============================== Constructors ================================== //


    public User(String name, String email, String password, boolean isAdmin) {
        this.fullName = name;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        rants = new ArrayList<>();
        seenRants = new ArrayList<>();
        groupOwners = new ArrayList<>();
        groupModerators = new ArrayList<>();
        groupMembers = new ArrayList<>();
        likedTags = new ArrayList<>();
        this.wantsProfanity = true;
    }

    public User() {
        rants = new ArrayList<>();
    }

    // =============================== Getters and Setters for each field ================================== //

    public int getId(){
        return id;
    }

    public void setId(int id){ this.id = id; }

    public String getFullName(){ return fullName; }

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

    public List<String> getLikedTags() { return likedTags;}

    public void addLikedTags(String newTag) { this.likedTags.add(newTag);}

    public List<Rant> getRants() { return rants; }

    public void setRants(List<Rant> rants) {
        this.rants = rants;
    }

    public void addRants(Rant rant){
        this.rants.add(rant);
    }

    public List<Rant> getSeenRants() { return seenRants; }

    public void setSeenRants(List<Rant> seenRants) {
        this.seenRants = seenRants;
    }

    public void addSeenRants(Rant rant){
        this.seenRants.add(rant);
    }

    public List<Group> getGroupMembers() { return groupMembers; }

    public void addGroupMember(Group group) {
        this.groupMembers.add(group);
    }

    public void removeGroupMember(Group group) {
        if (this.groupMembers.contains(group)) {
            this.groupMembers.remove(group);
        }

    }

    public void setWantsProfanity(boolean flag) {
        this.wantsProfanity = flag;
    }

    public boolean getWantsProfanity() {
        return this.wantsProfanity;
    }
}
