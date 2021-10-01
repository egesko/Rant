package tables.demo;

// Import Java libraries
import java.util.List;
import java.util.ArrayList;

// import junit/spring tests
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.http.MediaType;
import tables.demo.Group.Group;
import tables.demo.Group.GroupController;
import tables.demo.Group.GroupRepository;
import tables.demo.Rant.RantRepository;
import tables.demo.User.User;
import tables.demo.User.UserRepository;

// import mockito related
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@WebMvcTest(GroupController.class)
public class GroupTest {

    @TestConfiguration
    static class GroupConfiguration {
        @Bean
        GroupRepository getRepo() { return mock(GroupRepository.class); }

        @Bean
        RantRepository getRRepo() { return mock(RantRepository.class); }

        @Bean
        UserRepository getURepo() { return mock(UserRepository.class); }

        @Bean
        User getUser() { return mock(User.class); }

    }
    @Autowired
    private MockMvc controller;

    @Autowired
    GroupRepository repo;

    @Autowired
    RantRepository rRepo;

    @Autowired
    UserRepository URepo;


    @Test
    public void testCreate() throws Exception {
        List<Group> r = new ArrayList<>();

        when(repo.findAll()).thenReturn(r);


        when(repo.save((Group)any(Group.class)))
                .thenAnswer(x -> {
                    Group g = x.getArgument(0);
                    r.add(g);
                    return null;
                });


        when(URepo.findById((int)any(int.class))).thenAnswer(x -> {
                    User u = new User("beeb", "been", "dk", false);
                    return u;
                });

        JSONObject obj = new JSONObject();
        obj.put("userID", 1);
        obj.put("groupName", "beeb");
        obj.put("information", "idk");
        controller.perform(post("/groups/createFor").contentType(MediaType.APPLICATION_JSON).content(String.valueOf(obj))).andExpect(status().isOk()).andExpect(jsonPath("message", is("success")));
    }

    @Test
    public void testAddOwner() throws Exception {
        List<Group> r = new ArrayList<>();

        when(repo.findAll()).thenReturn(r);


        when(repo.save((Group)any(Group.class)))
                .thenAnswer(x -> {
                    Group g = x.getArgument(0);
                    r.add(g);
                    return null;
                });

        when(repo.findById((int)any(int.class))).thenAnswer(y -> {
            User u = new User("beeb", "been", "dk", false);
            Group g1 = new Group("test", "idk", u);
            return g1;
        });

        when(URepo.existsUserByEmail((String)any(String.class))).thenAnswer(x -> {
            //User u = new User("beeb", "been", "dk", false);
            return true;
        });

        when(URepo.findByEmail((String)any(String.class))).thenAnswer(y -> {
            User u = new User("beeb", "been", "dk", false);
            Group g1 = new Group("test", "idk", u);
            return u;
        });

        JSONObject obj = new JSONObject();
        obj.put("groupID", 1);
        obj.put("userName", "been");
        obj.put("isModerator", true);
        controller.perform(post("/groups/addUser").contentType(MediaType.APPLICATION_JSON).content(String.valueOf(obj))).andExpect(status().isOk()).andExpect(jsonPath("message", is("user already in group")));
    }

    @Test
    public void testAddNotFound() throws Exception {
        List<Group> r = new ArrayList<>();

        when(repo.findAll()).thenReturn(r);


        when(repo.save((Group)any(Group.class)))
                .thenAnswer(x -> {
                    Group g = x.getArgument(0);
                    r.add(g);
                    return null;
                });

        when(repo.findById((int)any(int.class))).thenAnswer(y -> {
            User u = new User("beeb", "been", "dk", false);
            Group g1 = new Group("test", "idk", u);
            return g1;
        });

        when(URepo.existsUserByEmail((String)any(String.class))).thenAnswer(z -> {
            return false;
        });


        JSONObject obj = new JSONObject();
        obj.put("groupID", 1);
        obj.put("userName", "beeb");
        obj.put("isModerator", true);
        controller.perform(post("/groups/addUser").contentType(MediaType.APPLICATION_JSON).content(String.valueOf(obj))).andExpect(status().isOk()).andExpect(jsonPath("message", is("user not found")));
    }

    @Test
    public void testForUser() throws Exception {
        List<Group> r = new ArrayList<>();

        when(repo.findAll()).thenReturn(r);


        when(repo.save((Group)any(Group.class)))
                .thenAnswer(x -> {
                    Group g = x.getArgument(0);
                    r.add(g);
                    return null;
                });


        when(URepo.findById((int)any(int.class))).thenAnswer(x -> {
            User u = new User("beeb", "been", "dk", false);
            Group g1 = new Group("test", "idk", u);
            u.addGroupMember(g1);
            return u;
        });



        JSONObject obj = new JSONObject();
        obj.put("userID", 1);
        JSONArray arr = new JSONArray();
        arr.add(obj);
        controller.perform(post("/groups/forUser").contentType(MediaType.APPLICATION_JSON).content(String.valueOf(arr))).andExpect(status().isOk()).andExpect(jsonPath("$[0].groupName", is("test")));
    }


}