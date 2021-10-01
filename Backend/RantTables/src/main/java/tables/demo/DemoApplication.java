package tables.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tables.demo.Group.GroupRepository;

import tables.demo.User.UserRepository;
import tables.demo.Rant.RantRepository;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

//    @Test
//    public void testRantGetTitle()
//    {
//
//        //MOCK THE RANTS
//        Rant rnt = mock(Rant.class);
//        rnt.
//        when(rnt.)
//
//
//
//
//    }

    @Bean
    CommandLineRunner initUser(UserRepository userRepository, RantRepository rantRepository, GroupRepository groupRepository) {
        return args -> {
//            User user1 = new User("Aryan", "aryanp@iastate.edu", "aryanp", true);
//            User user2 = new User("Damandeep", "damanr@iastate.edu", "damanr", true);
//            User user3 = new User("Ege", "ege@iastate.edu", "ege", true);
//            User user4 = new User("Sergio", "sergiopv@iastate.edu", "sergiopv", true);
//            userRepository.save(user1);
//            userRepository.save(user2);
//            userRepository.save(user3);
//            userRepository.save(user4);
//            Group group1 = new Group("ISU", "Iowa State Group", user1);
//            Group group2 = new Group("Iowa", "Group for the state of Iowa", user2);
//            Group group3 = new Group("Politics", "Group", user1);
//            groupRepository.save(group1);
//            groupRepository.save(group2);
//            groupRepository.save(group3);
//            List<Group> groups = new ArrayList<>();
//            List<Group> empty = new ArrayList<>();
//            groups.add(group1);
//            groups.add(group2);
//            Rant rant1 = (new Rant("math", "I hate math", Arrays.asList(new String[]{"politics", "world"}), false, groups));
//            for (Group group : groups) {
//                group.addRants(rant1);
//            }
//            rantRepository.save(rant1);
//            rantRepository.save(new Rant("english", "I hate english", Arrays.asList(new String[]{"politics", "world"}), true, empty));
//            Rant rant2 = (new Rant("science", "I hate science", Arrays.asList(new String[]{"politics", "world"}), true, groups));
//            for (Group group : groups) {
//                group.addRants(rant2);
//            }
//            rantRepository.save(rant2);
//            rantRepository.save(new Rant("ss", "I hate social studies", Arrays.asList(new String[]{"politics", "world"}), true, empty));
//            userRepository.save(user1);
//            groupRepository.save(group1);
//            user1.addGroupMember(group1);
//            user1.addGroupMember(group3);
//            user2.addGroupMember(group2);
//            user1.addRants(rantRepository.findById(1));
//            rantRepository.findById(1).setUser(user1);
//            user1.addRants(rantRepository.findById(2));
//            rantRepository.findById(2).setUser(user2);
//            user2.addRants(rantRepository.findById(3));
//            rantRepository.findById(3).setUser(user3);
//            user3.addRants(rantRepository.findById(4));
//            rantRepository.findById(4).setUser(user4);
//            userRepository.save(user1);
//            userRepository.save(user2);
//            userRepository.save(user3);
//            userRepository.save(user4);
        };
    }
}
