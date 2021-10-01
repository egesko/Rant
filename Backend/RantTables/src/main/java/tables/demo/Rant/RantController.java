package tables.demo.Rant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.models.auth.In;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tables.demo.Group.Group;
import tables.demo.Group.GroupRepository;
import tables.demo.User.User;
import tables.demo.User.UserRepository;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Api(value = "RantController", description = "REST APIs related to Rant Entity")
@RestController
public class RantController {

    @Autowired
    RantRepository rantRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserRepository userRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @ApiOperation(value = "Get all rants in database")
    @GetMapping(path = "/rants")
    List<Rant> getAllRants(){
        return rantRepository.findAll();
    }

    @ApiOperation(value = "Get rant for specific id")
    @GetMapping(path = "/rants/{id}")
    Rant getRantById( @PathVariable int id){
        return rantRepository.findById(id);
    }

    @ApiOperation(value = "Create a rant tied to given user id")
    @PostMapping(path = "/rants/create")
    String createRant(@RequestBody String requestString){

        JSONParser parser = new JSONParser();
        JSONObject requestObj = null;
        String title = null;
        String rant = null;
        List<String> tags = null;
        Integer userID = null;
        boolean postAll;
        List<Integer> groupIds = new ArrayList<>();
        List<Group> groups = new ArrayList<>();
        try {
            requestObj = (JSONObject) parser.parse(requestString);
            title = (String) requestObj.get("title");
            rant = (String) requestObj.get("rant");
            tags = (List<String>) requestObj.get("tags");
            userID = (Integer) requestObj.get("userID");
            postAll = (boolean) requestObj.get("postAll");
            groupIds = (List<Integer>) requestObj.get("groups");
        }
        catch (Exception e)
        {
            System.out.println(requestString);
            System.out.println("Exception occurred. Either because one of the necessary fields are not provided or because there is a type mismatch.");
            return failure;
        }

        for (Integer id : groupIds) {
            groups.add(groupRepository.findById(id));
        }
        Rant finalRant = new Rant(title, rant, tags, postAll, groups);
        for (Group group : groups) {
            group.addRants(finalRant);
        }
        finalRant.setUser(userRepository.findById(userID));
        userRepository.findById(userID).addRants(finalRant);
        rantRepository.save(finalRant);
        return success;
    }


    @ApiOperation(value = "Update rant at specific id")
    @PutMapping("/rants/{id}")
    Rant updateRant(@PathVariable int id, @RequestBody Rant request){
        Rant rant = rantRepository.findById(id);
        if(rant == null)
            return null;
        rantRepository.save(request);
        return rantRepository.findById(id);
    }


    @ApiOperation(value = "Increment like for rant at given id, and add the tags to the user's likedTags list")
    @PostMapping("/rants/{id}/like")
    JSONObject likeRant(@PathVariable int id, @RequestBody Map<String,Integer> userInfo){
        Rant rant = rantRepository.findById(id);
        if(rant == null)
            return null;

        User myUser;

        try {
            myUser = userRepository.findById(userInfo.get("userID"));
        }
        catch (Exception e)
        {
            System.out.println("Problem occurred when trying to find user. " +
                    "Check likeRant method in RantController.java. " +
                    "(Possible type mismatch)");

            return null; //TO BE CHANGED
        }

        rant.addLike();




        List<String> tagsFromRant = rant.getTags();
        List<String> userLikedTags = myUser.getLikedTags();

        for (String s : tagsFromRant)
        {
            if(!userLikedTags.contains(s))
            {
                myUser.addLikedTags(s);
            }
        }
        userRepository.save(myUser);
        JSONObject obj = new JSONObject();
        obj.put("likes", rant.getLikes());
        return obj;
    }


    @ApiOperation(value = "Return rants for a user")
    @PostMapping("/rants/forUser")
    List<Rant> getRantForUser(@RequestBody Map<String, Integer> [] request) {
        Integer userID = request[0].get("userID");
        return userRepository.findById(userID).getRants();
    }

    //TO BE MODIFIED
    @ApiOperation(value = "Increment dislike for rant at given id")
    @PostMapping("/rants/{id}/dislike")
    JSONObject dislikeRant(@PathVariable int id, @RequestBody Map<String,Integer> userInfo){
        Rant rant = rantRepository.findById(id);
        if(rant == null)
            return null;

        User myUser;

        try {
            myUser = userRepository.findById(userInfo.get("userID"));
        }
        catch (Exception e)
        {
            System.out.println("Problem occurred when trying to find user. " +
                    "Check likeRant method in RantController.java. " +
                    "(Possible type mismatch)");
            return null; //TO BE CHANGED
        }
        rant.addDislike();






        List<String> tagsFromRant = rant.getTags();
        List<String> userLikedTags = myUser.getLikedTags();

        for (String s : tagsFromRant)
        {
            if(!userLikedTags.contains(s))
            {
                myUser.addLikedTags(s);
            }
        }
        userRepository.save(myUser);
        JSONObject obj = new JSONObject();
        obj.put("dislikes", rant.getDisLikes());
        return obj;
    }

    @ApiOperation(value = "Return 5 random rants that have likedTags for the ForYou page")
    @PostMapping("/rants/forYou")
    JSONArray returnRants(@RequestBody Map<String,Integer> [] userInfo) {
        
        //CONSTANTS
        final int MAXRETURNSIZE = 4;
        
        //Lists
        List<Rant> allRants = rantRepository.findAll();
        List<Rant> matchingRants = new ArrayList<>();
        List<Rant> returnRants = new ArrayList<>();
        List<String> rantTags;

        //User
        User myUser;



        try {
            myUser = userRepository.findById(userInfo[0].get("userID"));
        }
        catch (Exception e)
        {
            System.out.println("Problem occurred when trying to find user. " +
                    "Check returnRants method in RantController.java. " +
                    "(Possible type mismatch or User Doesn't Exist)");

            return null; //TO BE CHANGED
        }

        if (myUser.getLikedTags().size() == 0) { //If user didn't like any rants with tags so far
            for (int j = 0; j < allRants.size(); j++) {
                Random r = new Random();
                int num = r.nextInt(allRants.size());
                returnRants.add(allRants.get(num));
                allRants.remove(num);
                if (j == MAXRETURNSIZE) {
                    break;
                }
            }
        }
        List<String> userLikedTags = myUser.getLikedTags();

        //Creating an array of Rants that have user liked tags (matchingRants)
        for (int i = 0; i < allRants.size(); i++)
        {
            rantTags = allRants.get(i).getTags();
            for (int j = 0; j <userLikedTags.size();j++)
            {
                if(rantTags.contains(userLikedTags.get(j)))
                {
                    matchingRants.add(allRants.get(i));
                }
            }
        }

        for (int j = 0; j < matchingRants.size(); j++) {
            Random r = new Random();
            int num = r.nextInt(matchingRants.size());
            returnRants.add(matchingRants.get(num));
            matchingRants.remove(num);
            if (j == MAXRETURNSIZE) {
                break;
            }
        }

        System.out.println(userLikedTags.size());
        //IF LIKEDTAGS IS EMPTY (WROTE THIS WHILE I WAS SLEEP DEPRIVED, MAKE SURE IT'S CORRECT)
        if(userLikedTags.size()<3)
        {
            allRants = rantRepository.findAll();
            returnRants = new ArrayList<>();
            for (int j = 0; j < allRants.size(); j++) {
                Random r = new Random();
                int num = r.nextInt(allRants.size());
                returnRants.add(allRants.get(num));
                allRants.remove(num);
                if (j == MAXRETURNSIZE) {
                    break;
                }
            }
        }
        //----------------------------------------------------------------------------------------


        JSONArray myJsonArray = new JSONArray();
        Rant myRant;
        JSONObject myJsonObject;
        JSONArray myJsonArrayForTags;
        int vote;
        for (int i = 0; i< returnRants.size();i++)
        {
            myJsonObject = new JSONObject();
            myJsonArrayForTags = new JSONArray();
            myRant = returnRants.get(i);
            myJsonObject.put("id", myRant.getId());
            myJsonObject.put("rant", myRant.getRant());
            myJsonObject.put("title", myRant.getTitle());
            myJsonObject.put("likes", myRant.getLikes());
            myJsonObject.put("disLikes", myRant.getDisLikes());
            myJsonObject.put("isProfanity", myRant.getIsProfanity());
            if(myRant.getLikeList().contains(myUser))
            {
                myJsonObject.put("vote", 1);
            }
            else if(myRant.getDislikeList().contains(myUser))
            {
                myJsonObject.put("vote", -1);
            }
            else
            {
                myJsonObject.put("vote", 0);
            }

            for(int j = 0; j<myRant.getTags().size();j++)
            {
                myJsonArrayForTags.add(myRant.getTags().get(j));
            }
            myJsonObject.put("tags",myJsonArrayForTags);

            myJsonArray.add(myJsonObject);
        }

        return myJsonArray;
    }

    @ApiOperation(value = "Return most liked ")
    @GetMapping("/rants/mostLiked")
    List<Rant> returnMostLiked() {

        //CONSTANTS
        final int MAXRETURNSIZE = 4; //4+1 = 5

        List<Rant> OGList = rantRepository.findAll();
        List<Rant> ReturnList = new ArrayList<>();

        for (int i = 0; i < OGList.size(); i++) {

            for(int j = 1;j<OGList.size();j++)
            {
                if(OGList.get(j-1).getLikes()>OGList.get(j).getLikes())
                {
                    Rant largerValue = OGList.get(j-1);
                    OGList.set(j-1,OGList.get(j));
                    OGList.set(j,largerValue);
                }
            }
        }

        int Test;

        for(int i = OGList.size(); i > 0;i--)
        {
            ReturnList.add(OGList.get(i-1));

            if (i == OGList.size() - MAXRETURNSIZE) {
                break;
            }
        }


        return ReturnList;
    }



    @ApiOperation(value = "Return most disliked ")
    @GetMapping("/rants/mostDisliked")
    List<Rant> returnMostDisliked() {

        //CONSTANTS
        final int MAXRETURNSIZE = 4; //4+1 = 5

        List<Rant> OGList = rantRepository.findAll();
        List<Rant> ReturnList = new ArrayList<>();

        for (int i = 0; i < OGList.size(); i++) {

            for(int j = 1;j<OGList.size();j++)
            {
                if(OGList.get(j-1).getDisLikes()>OGList.get(j).getDisLikes())
                {
                    Rant largerValue = OGList.get(j-1);
                    OGList.set(j-1,OGList.get(j));
                    OGList.set(j,largerValue);
                }
            }
        }

        int Test;



        for(int i = OGList.size(); i > 0;i--)
        {
            ReturnList.add(OGList.get(i-1));

            if (i == OGList.size() - MAXRETURNSIZE) {
                break;
            }
        }


        return ReturnList;
    }
}











