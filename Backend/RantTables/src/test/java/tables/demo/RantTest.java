package tables.demo;

// Import Java libraries
import java.util.Arrays;
import java.util.Collections;
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
import tables.demo.Rant.Rant;
import tables.demo.Rant.RantController;
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
@WebMvcTest(RantController.class)
public class RantTest {

    @TestConfiguration
    static class RantConfiguration{

        @Bean
        GroupRepository getRepo() { return mock(GroupRepository.class); }

        @Bean
        RantRepository getRRepo() { return mock(RantRepository.class); }

        @Bean
        UserRepository getURepo() { return mock(UserRepository.class); }

    }
    @Autowired
    private MockMvc controller;

    @Autowired
    GroupRepository GRepo;

    @Autowired
    RantRepository RRepo;

    @Autowired
    UserRepository URepo;

    //EGE DEMIR
    @Test
    public void testLike() throws Exception
    {
        List<Rant> rList = new ArrayList<>();
        List<Rant> uList = new ArrayList<>();

        when(RRepo.findAll()).thenReturn(rList);

        when(RRepo.save((Rant)any(Rant.class)))
                .thenAnswer(x -> {
                    Rant r = x.getArgument(0);
                    rList.add(r);
                    return null;
                });

        when(URepo.findById((int)any(int.class))).thenAnswer(x -> {
            User u = new User("name","email","pass",false);//new Rant("title","rant", Arrays.asList(new String[]{"foo", "bar"}),true, Collections.emptyList());
            return u;
        });

        when(RRepo.findById((int)any(int.class))).thenAnswer(x -> {
            Rant r = new Rant("title","rant", Arrays.asList(new String[]{"foo", "bar"}),true, Collections.emptyList());
            return r;
        });

        JSONObject obj = new JSONObject();
        obj.put("userID", 1);
        controller.perform(post("/rants/1/like").contentType(MediaType.APPLICATION_JSON).content(String.valueOf(obj))).andExpect(status().isOk()).andExpect(jsonPath("likes", is(1)));




    }

    //EGE DEMIR
    @Test
    public void testDislike() throws Exception
    {
        List<Rant> rList = new ArrayList<>();
        List<Rant> uList = new ArrayList<>();

        when(RRepo.findAll()).thenReturn(rList);

        when(RRepo.save((Rant)any(Rant.class)))
                .thenAnswer(x -> {
                    Rant r = x.getArgument(0);
                    rList.add(r);
                    return null;
                });

        when(URepo.findById((int)any(int.class))).thenAnswer(x -> {
            User u = new User("name","email","pass",false);
            return u;
        });

        when(RRepo.findById((int)any(int.class))).thenAnswer(x -> {
            Rant r = new Rant("title","rant", Arrays.asList(new String[]{"foo", "bar"}),true, Collections.emptyList());
            rList.add(r);
            return r;
        });

        JSONObject obj = new JSONObject();
        obj.put("userID", 1);
        controller.perform(post("/rants/1/dislike").contentType(MediaType.APPLICATION_JSON).content(String.valueOf(obj))).andExpect(status().isOk()).andExpect(jsonPath("dislikes", is(1)));

    }

    //EGE DEMIR
    @Test
    public void testGetRantsForUser() throws Exception {

        List<Rant> rList = new ArrayList<>();

        when(RRepo.findAll()).thenReturn(rList);

        when(RRepo.save((Rant)any(Rant.class)))
                .thenAnswer(x -> {
                    Rant r = x.getArgument(0);
                    rList.add(r);
                    return null;
                });


        when(URepo.findById((int)any(int.class))).thenAnswer(x -> {
            User u = new User("beeb", "been", "dk", false);
            Rant g1 = new Rant("title","rant", Arrays.asList(new String[]{"foo", "bar"}),true, Collections.emptyList());
            u.addRants(g1);
            return u;
        });

        JSONObject obj = new JSONObject();
        obj.put("userID", 1);
        JSONArray arr = new JSONArray();
        arr.add(obj);
        controller.perform(post("/rants/forUser").contentType(MediaType.APPLICATION_JSON).content(String.valueOf(arr))).andExpect(status().isOk()).andExpect(jsonPath("$[0].title", is("title")));
    }

//    @Test
//    public void testCreate() throws Exception
//    {


}
