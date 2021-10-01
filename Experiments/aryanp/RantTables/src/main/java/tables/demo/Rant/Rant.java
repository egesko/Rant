package tables.demo.Rant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import tables.demo.User.User;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Rant {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String rant;
    private int likes;
    private int dislikes;
    private boolean isProfinity;
    private Date lastModified;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    // =============================== Constructors ================================== //

    public Rant(){

    }

    public Rant(String rant, boolean isProfinity, Date lastModified) {
        this.rant = rant;
        this.isProfinity = isProfinity;
        this.lastModified = lastModified;
        this.likes = 0;
        this.dislikes = 0;
    }


    // =============================== Getters and Setters for each field ================================== //


    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getRant() { return rant; }

    public void setRant(String rant) { this.rant = rant; }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public int getLikes() {
        return likes;
    }

    public void addLike() {
        this.likes += 1;
    }

    public int getDisikes() {
        return dislikes;
    }

    public void addDislike() { this.dislikes += 1; }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean getIsProfanity() {
        return isProfinity;
    }

    public void setIsProfinity(boolean isProfinity) {
        this.isProfinity = isProfinity;
    }

}
