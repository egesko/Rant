package tables.demo.Group;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tables.demo.Rant.Rant;
import tables.demo.User.User;



import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Table(name="gr0up")
public class Group {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String groupName;
    @JsonIgnore
    private String information;
    @JsonIgnore
    private String groupKey;

    private int totalMembers;

    @ManyToMany
    @JsonIgnore
    private List<User> members;


    @ManyToOne
    @JsonIgnore
    private User groupOwner;

    @ManyToMany
    @JsonIgnore
    private List<User> moderators;

    @ManyToMany
    @JsonIgnore
    private List<Rant> rants;
    // =============================== Constructors ================================== //

    public Group() {

    }

    public Group(String groupName, String information, User owner) {
        Random rnd = new Random();
        this.groupKey = String.format("%09d", rnd.nextInt(999999999));
        this.groupName = groupName;
        this.information = information;
        this.groupOwner = owner;
        this.members = new ArrayList<>();
        this.moderators = new ArrayList<>();
        this.rants = new ArrayList<>();
    }

    public Group(String groupName, String information) {
        Random rnd = new Random();
        this.groupKey = String.format("%09d", rnd.nextInt(999999999));
        this.groupName = groupName;
        this.information = information;
        this.members = new ArrayList<>();
        this.moderators = new ArrayList<>();
    }



    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getGroupName() { return groupName; }

    public void setGroupName(String groupName) { this.groupName = groupName; }

    public String getInformation() { return information; }

    public void setInformation(String information) { this.information = information; }

    public User getGroupOwner() { return this.groupOwner; }

    public void setOwner(User user) {
        this.groupOwner = user;
    }

    public void addModerator(User user) {
        if (this.members.contains(user)) {
            this.members.remove(user);
        } else {
            user.addGroupMember(this);
        }
        if (!this.moderators.contains(user)) {
            this.moderators.add(user);
            this.incrementMembers();
        }

    }

    public List<User> getModerators() { return moderators; }

    public List<User> getMembers() { return members; }

    public void addMember(User user) {
        if (this.moderators.contains(user)) {
            this.moderators.remove(user);
        }
        if (!this.members.contains(user)) {
            this.members.add(user);
            user.removeGroupMember(this);
            user.addGroupMember(this);
            this.incrementMembers();
        }
    }

    public String getGroupKey() { return this.groupKey; }

    public List<Rant> getRants() { return rants; }

    public void addRants(Rant rant) { this.rants.add(rant); }

    public void incrementMembers() { this.totalMembers++; }

    public int getTotalMembers() {
        int mem = 1;
        mem += this.moderators.size();
        mem += this.members.size();
        this.totalMembers = mem;
        return this.totalMembers;
    }

    public void setTotalMembers(int total) { this.totalMembers = total; }
}

