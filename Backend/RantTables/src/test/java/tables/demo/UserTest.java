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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultMatcher;
import tables.demo.Group.Group;
import tables.demo.Group.GroupController;
import tables.demo.Group.GroupRepository;
import tables.demo.Rant.RantRepository;
import tables.demo.User.User;
import tables.demo.User.UserController;
import tables.demo.User.UserRepository;

// import mockito related
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserTest {
    @TestConfiguration
    static class GroupConfiguration {
        @Bean
        UserRepository getURepo() { return mock(UserRepository.class); }

        @Bean
        GroupRepository getRepo() { return mock(GroupRepository.class); }

        @Bean
        RantRepository getRRepo() { return mock(RantRepository.class); }

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
    public void testLogin() throws Exception {
        List<User> r = new ArrayList<>();

        when(URepo.findAll()).thenReturn(r);


        when(URepo.save((User)any(User.class)))
                .thenAnswer(x -> {
                    User u = x.getArgument(0);
                    r.add(u);
                    return null;
                });


        when(URepo.findByEmail((String)any(String.class))).thenAnswer(x -> {
            User u = new User("beeb", "test@gmail.com", "test123", false);
            URepo.save(u);
            return  u;
        });

        JSONObject obj = new JSONObject();
        obj.put("email", "test@gmail.com");
        obj.put("password", "test123");

        controller.perform(post("/users/login").contentType(MediaType.APPLICATION_JSON).content(String.valueOf(obj))).andExpect(status().isOk()).andExpect(content().json("{result=success, id=0}"));

    }

    //EGE DEMIR
    @Test
    public void testGetUserById() throws Exception {

        List<User> uList = new ArrayList<>();

        when(URepo.findAll()).thenReturn(uList);

        when(URepo.save((User)any(User.class)))
                .thenAnswer(x -> {
                    User u = x.getArgument(0);
                    uList.add(u);
                    return null;
                });


        when(URepo.findById((int)any(int.class))).thenAnswer(x -> {
            User u = new User("name","email","pass",false);
            return u;
        });


        controller.perform(get("/users/1")).andExpect(status().isOk()).andExpect(jsonPath("fullName", is("name")));
    }





}
