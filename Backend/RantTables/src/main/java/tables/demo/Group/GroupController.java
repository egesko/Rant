package tables.demo.Group;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tables.demo.Rant.Rant;
import tables.demo.Rant.RantRepository;
import tables.demo.User.User;
import tables.demo.User.UserRepository;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(value = "GroupController", description = "REST APIs related to Group Entity")
@RestController
public class GroupController {
    @Autowired
    RantRepository rantRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserRepository userRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @ApiOperation(value = "Get all groups in database")
    @GetMapping(path = "/groups")
    List<Group> getAllGroups(){
        return groupRepository.findAll();
    }

    @ApiOperation(value = "Get group for specific id")
    @GetMapping(path = "/groups/{id}")
    Group getGroupById( @PathVariable int id){
        return groupRepository.findById(id);
    }

    @ApiOperation(value = "Add a group to the database with owners id")
    @PostMapping(path = "/groups/createFor")
    String createGroup(@RequestBody String requestString){
        JSONParser parser = new JSONParser();
        JSONObject requestObj = null;
        String groupName = null;
        String information = null;
        Integer userID = null;

        try {
            requestObj = (JSONObject) parser.parse(requestString);
            userID = (Integer)  requestObj.get("userID");
            groupName = (String) requestObj.get("groupName");
            information = (String) requestObj.get("information");
        }
        catch (Exception e)
        {
            System.out.println("Exception occured. Either because one of the necessary fields are not provided or because there is a type mismatch.");
            return failure;
        }
        Group realGroup = new Group(groupName, information);
        realGroup.setOwner(userRepository.findById(userID));
        realGroup.incrementMembers();
        userRepository.findById(userID).addGroupMember(realGroup);
        groupRepository.save(realGroup);
        return success;

//        if (group == null)
//            return failure;
//        Group realGroup = new Group(group.getGroupName(), group.getInformation());
//        realGroup.setOwner(userRepository.findById(userId));
//        userRepository.findById(userId).addGroupMember(realGroup);
//        groupRepository.save(realGroup);
//        return success;
    }

    @ApiOperation(value = "Update group at specific id")
    @PutMapping("/groups/{id}")
    Group updateGroup(@PathVariable int id, @RequestBody Group request){
        Group group = groupRepository.findById(id);
        if(group == null)
            return null;
        groupRepository.save(request);
        return groupRepository.findById(id);
    }

    @ApiOperation(value = "Add a user as a member of a group")
    @PostMapping("/groups/addUser")
    String addMember(@RequestBody String requestString) {
        JSONParser parser = new JSONParser();
        JSONObject requestObj = null;
        String userName = null;
        Integer groupID = null;
        boolean isModerator = false;

        try {
            requestObj = (JSONObject) parser.parse(requestString);
            userName = (String)  requestObj.get("userName");
            groupID = (Integer) requestObj.get("groupID");
            isModerator = (boolean) requestObj.get("isModerator");
        }
        catch (Exception e)
        {
            System.out.println("Exception occured. Either because one of the necessary fields are not provided or because there is a type mismatch.");
            return failure;
        }
        Group group = groupRepository.findById(groupID);
        if (!userRepository.existsUserByEmail(userName)) {
            return "{\"message\":\"user not found\"}";
        }
        User user = userRepository.findByEmail(userName);

        if (user.getEmail().equals(group.getGroupOwner().getEmail())) {
            return "{\"message\":\"user already in group\"}";
        }
        if (isModerator) {
            if (group.getModerators().contains(user)) {
                return "{\"message\":\"user already in group\"}";
            }
            group.addModerator(user);
        } else {
            if (group.getMembers().contains(user)) {
                return "{\"message\":\"user already in group\"}";
            }
            group.addMember(user);
        }
        group.getTotalMembers();
        groupRepository.save(group);
        return success;
    }

    @ApiOperation(value = "get users in group and their role")
    @PostMapping(path = "/groups/getUsers")
    List<JSONObject> getUsers (@RequestBody Map<String, Integer> [] request) {
        Integer groupID = request[0].get("groupID");
        Group group = groupRepository.findById(groupID);
        List<JSONObject> returnList = new ArrayList<>();
        List<User> members = group.getMembers();
        List<User> moderators = group.getModerators();
        User owner = group.getGroupOwner();

        JSONObject jOwner = new JSONObject();
        jOwner.put("userID", owner.getId());
        jOwner.put("email", owner.getEmail());
        jOwner.put("role", "owner");
        returnList.add(jOwner);

        for (int i = 0; i < moderators.size(); i++) {
            JSONObject add = new JSONObject();
            add.put("userID", moderators.get(i).getId());
            add.put("email", moderators.get(i).getEmail());
            add.put("role", "moderator");
            returnList.add(add);
        }

        for (int i = 0; i < members.size(); i++) {
            JSONObject add = new JSONObject();
            add.put("userID", members.get(i).getId());
            add.put("email", members.get(i).getEmail());
            add.put("role", "member");
            returnList.add(add);
        }

        return returnList;
    }
 // test
    @ApiOperation(value = "Return groups for a user")
    @PostMapping("/groups/forUser")
    List<Group> getGroupsForUser(@RequestBody Map<String, Integer> [] request) {
//        System.out.println(request[0]);
//        JSONParser parser = new JSONParser();
//        JSONObject requestObj = null;
//        Integer userID = null;
//        try {
//            requestObj = (JSONObject) parser.parse(requestString.get(0));
//            userID = (Integer) requestObj.get("userID");
//        }
//        catch (Exception e)
//        {
//            System.out.println("Exception occured. Either because one of the necessary fields are not provided or because there is a type mismatch.");
//        }
        Integer userID = request[0].get("userID");
        return userRepository.findById(userID).getGroupMembers();
    }





}
