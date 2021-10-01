package tables.demo.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import tables.demo.Rant.Rant;
import tables.demo.Rant.RantRepository;
@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RantRepository rantRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping(path = "/users/login")
    List<String> getUsersLogin(){

        List<User> users = userRepository.findAll();
        List<String> userpass = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            userpass.add(users.get(i).getEmail() + " " + users.get(i).getPassword());
        }
        return userpass;
    }

    @GetMapping(path = "/users/{id}")
    User getUserById( @PathVariable int id){
        return userRepository.findById(id);
    }

    @PostMapping(path = "/users")
    String createUser(@RequestBody User user){
        if (user == null)
            return failure;
        userRepository.save(user);
        return success;
    }

    @PutMapping("/users/{id}")
    User updateUser(@PathVariable int id, @RequestBody User request){
        User user = userRepository.findById(id);
        if(user == null)
            return null;
        userRepository.save(request);
        return userRepository.findById(id);
    }

    @DeleteMapping(path = "/users/{id}")
    String deleteLaptop(@PathVariable int id){
        userRepository.deleteById(id);
        return success;
    }
}
