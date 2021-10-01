package tables.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tables.demo.Rant.Rant;
import tables.demo.User.User;

import java.util.Date;
import tables.demo.User.UserRepository;
import tables.demo.Rant.RantRepository;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    CommandLineRunner initUser(UserRepository userRepository, RantRepository rantRepository) {
        return args -> {
//            User user1 = new User("Aryan", "aryanp@iastate.edu", "aryanp", true);
//            User user2 = new User("Damandeep", "damanr@iastate.edu", "damanr", true);
//            User user3 = new User("Ege", "ege@iastate.edu", "ege", true);
//            User user4 = new User("Sergio", "sergiopv@iastate.edu", "sergiopv", true);
//            rantRepository.save(new Rant("I hate math", false, new Date()));
//            rantRepository.save(new Rant("I hate english", false, new Date()));
//            rantRepository.save(new Rant("I hate science", false, new Date()));
//            rantRepository.save(new Rant("I hate social studies", false, new Date()));
//            user1.addRants(rantRepository.findById(1));
//            user1.addRants(rantRepository.findById(2));
//            user2.addRants(rantRepository.findById(3));
//            user3.addRants(rantRepository.findById(4));
//            userRepository.save(user1);
//            userRepository.save(user2);
//            userRepository.save(user3);
//            userRepository.save(user4);
        };
    }
}
