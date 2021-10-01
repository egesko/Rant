package tables.demo.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tables.demo.Group.Group;
import tables.demo.Rant.Rant;
import tables.demo.Rant.RantRepository;

@Api(value = "UserController", description = "REST APIs related to User Entity")
@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RantRepository rantRepository;

    private final String successMessage = "{\"message\":\"success\"}";
    private final String failureMessage = "{\"message\":\"failure\"}";

    @ApiOperation(value = "Get all users in database")
    @GetMapping(path = "/users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }



    @ApiOperation(value = "Returns id if login is successful. Otherwise, returns error message")
    @PostMapping(path = "/users/login")
    String getUsersLogin(@RequestBody Map<String, String> userInfo){

        //Using a hashmap to create JSON Response
        HashMap<String,String> map = new HashMap<>();

        //Look for matching email and password
        User targetUser = userRepository.findByEmail(userInfo.get("email"));
        if(targetUser == null)//Meaning couldn't find any matching email.
        {
            return "{\"failure\":\"no email\"}";
        }
        try{
            if(targetUser.getPassword().equals(userInfo.get("password"))) //Checking if passwords match. If they do, return success and user id.
            {
                map.put("result","success");
                map.put("id",String.valueOf(targetUser.getId()));
                return map.toString();
            }
            else
            {
                return "{\"failure\":\"password dont match\"}";
            }


        } catch(Exception exception)
        {
            return "{\"failure\":\"unknown failure, notify me so I can debug this.\"}";
        }

    }



    @ApiOperation(value = "Get a user specified by id")
    @GetMapping(path = "/users/{id}")
    User getUserById( @PathVariable int id){

        return userRepository.findById(id);
    }

    @ApiOperation(value = "Create a user")
    @PostMapping(path = "/users")
    String createUser(@RequestBody String requestString){

        JSONParser parser = new JSONParser();
        JSONObject requestObj = null;
        String fullName = null;
        String email = null;
        String password = null;
        boolean isAdmin;

        try {
            requestObj = (JSONObject) parser.parse(requestString);
            fullName = (String) requestObj.get("fullName");
            email = (String) requestObj.get("email");
            password = (String) requestObj.get("password");
            isAdmin = (boolean) requestObj.get("isAdmin");
        }
        catch (Exception e)
        {
            System.out.println(requestString);
            System.out.println("Exception occurred. Either because one of the necessary fields are not provided or because there is a type mismatch.");
            return failureMessage;
        }
        if (fullName == null || userRepository.existsUserByEmail(email))
        {
            if(email == null)
                System.out.println("NULL USER");
            if(userRepository.existsUserByEmail(email))
            {
                System.out.println("email:" + email);
                System.out.println("EMAIL EXISTS");
            }
            return failureMessage;
        }

        User user = new User(fullName,email,password,isAdmin);
        userRepository.save(user);
        return successMessage;
    }

    @ApiOperation(value = "Update a user by id")
    @PutMapping("/users/{id}")
    User updateUser(@PathVariable int id, @RequestBody User request){
        User user = userRepository.findById(id);
        if(user == null)
            return null;
        userRepository.save(request);
        return userRepository.findById(id);
    }

    @ApiOperation(value = "Delete a user by id")
    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable int id){
        userRepository.deleteById(id);
        return successMessage;
    }

    @ApiOperation(value = "update profanity values")
    @PostMapping(path = "/users/setWantsProfanity")
    Map<String, Boolean> updateState(@RequestBody Map<String, Integer> userInfo) {
        User u = userRepository.findById(userInfo.get("userID"));
        int flag = userInfo.get("flag");
        if (flag == 1) {
            u.setWantsProfanity(true);
        } else if (flag == 2) {
            u.setWantsProfanity(false);
        }
        userRepository.save(u);
        Map<String, Boolean> returnVal = new HashMap<>();
        returnVal.put("state", u.getWantsProfanity());
        return  returnVal;
    }





}
