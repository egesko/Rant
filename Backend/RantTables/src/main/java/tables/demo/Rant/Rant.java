package tables.demo.Rant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.tool.schema.internal.GroupedSchemaMigratorImpl;
import tables.demo.Group.Group;
import tables.demo.User.User;

import javax.persistence.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

@Entity
public class Rant {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String rant;
    private String title;
    private int likes;
    private int dislikes;
    private boolean isProfanity;
    private Date lastModified;
    private boolean postAll;

    @ElementCollection
    private List<String> tags;

    @ManyToMany
    @JsonIgnore
    private List<Group> groups;

    // List of users that liked the rant
    @ManyToMany
    private List<User> likeList;

    // List of users that disliked the rant
    @ManyToMany
    private List<User> dislikeList;


    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    // =============================== Constructors ================================== //

    public Rant(){

    }

    public Rant(String title, String rant, List<String> tags, boolean postAll, List<Group> groups) {
        this.title = title;
        this.rant = rant;
        if (filterText(rant)) {
            this.isProfanity = true;
        } else {
            this.isProfanity = false;
        }
        this.tags = tags;
        this.lastModified = new Date();
        this.likes = 0;
        this.dislikes = 0;
        this.postAll = postAll;
        this.groups = groups;
        likeList = new ArrayList<>();
        dislikeList = new ArrayList<>();
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
    public void removeLike() {
        this.likes -= 1;
    }

    public int getDisLikes() {
        return dislikes;
    }

    public void addDislike() { this.dislikes += 1; }
    public void removeDisLike() {
        this.dislikes -= 1;
    }

    public List<User> getLikeList() { return likeList; }
    public void addLikeList(User myUser) { this.likeList.add(myUser); }
    public void removeLikeList(User myUser) { this.likeList.remove(myUser); }

    public List<User> getDislikeList() { return dislikeList; }
    public void addDislikeList(User myUser) { this.dislikeList.add(myUser); }
    public void removeDislikeList(User myUser) { this.dislikeList.remove(myUser); }

    public User getUser(User byId) {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean getIsProfanity() {
        return isProfanity;
    }

    public void setIsProfanity(boolean isProfanity) {
        this.isProfanity = isProfanity;
    }

    public List<String> getTags() { return tags; }

    public String getTitle() { return title; }

    public List<Group> getGroups() { return groups; }

    public void setGroups(List<Group> groups) { this.groups = groups; }

    static Map<String, String[]> words = new HashMap<>();

    static int largestWordLength = 0;

    public static void loadConfigs() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://docs.google.com/spreadsheets/d/1hIEi2YG3ydav1E06Bzf2mQbGZ12kh2fe4ISgLg_UBuM/export?format=csv").openConnection().getInputStream()));
            String line = "";
            int counter = 0;
            while((line = reader.readLine()) != null) {
                counter++;
                String[] content = null;
                try {
                    content = line.split(",");
                    if(content.length == 0) {
                        continue;
                    }
                    String word = content[0];
                    String[] ignore_in_combination_with_words = new String[]{};
                    if(content.length > 1) {
                        ignore_in_combination_with_words = content[1].split("_");
                    }

                    if(word.length() > largestWordLength) {
                        largestWordLength = word.length();
                    }
                    words.put(word.replaceAll(" ", ""), ignore_in_combination_with_words);

                } catch(Exception e) {
                    e.printStackTrace();
                }

            }
            System.out.println("Loaded " + counter + " words to filter out");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Iterates over a String input and checks whether a cuss word was found in a list, then checks if the word should be ignored (e.g. bass contains the word *ss).
     * @param input
     * @return
     */

    public static ArrayList<String> badWordsFound(String input) {
        if(input == null) {
            return new ArrayList<>();
        }



        input = input.replaceAll("1","i");
        input = input.replaceAll("!","i");
        input = input.replaceAll("3","e");
        input = input.replaceAll("4","a");
        input = input.replaceAll("@","a");
        input = input.replaceAll("5","s");
        input = input.replaceAll("7","t");
        input = input.replaceAll("0","o");
        input = input.replaceAll("9","g");


        ArrayList<String> badWords = new ArrayList<>();
        input = input.toLowerCase().replaceAll("[^a-zA-Z]", "");

        // iterate over each letter in the word
        for(int start = 0; start < input.length(); start++) {
            // from each letter, keep going to find bad words until either the end of the sentence is reached, or the max word length is reached.
            for(int offset = 1; offset < (input.length()+1 - start) && offset < largestWordLength; offset++)  {

                String wordToCheck = input.substring(start, start + offset);
                if(words.containsKey(wordToCheck)) {
                    // for example, if you want to say the word bass, that should be possible.
                    String[] ignoreCheck = words.get(wordToCheck);
                    boolean ignore = false;
                    for(int s = 0; s < ignoreCheck.length; s++ ) {
                        if(input.contains(ignoreCheck[s])) {
                            ignore = true;
                            break;
                        }
                    }
                    if(!ignore) {
                        badWords.add(wordToCheck);
                    }
                }
            }
        }


        for(String s: badWords) {
            System.out.println(s + " qualified as a bad word in a username");
        }
        return badWords;

    }

    public static boolean filterText(String input) {
        loadConfigs();
        ArrayList<String> badWords = badWordsFound(input);

        if(badWords.size() > 0) {
            return true;
        }
        return false;
    }
}
