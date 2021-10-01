package tables.demo.Rant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "RantController", description = "REST APIs related to Rant Entity")
@RestController
public class RantController {
    @Autowired
    RantRepository rantRepository;
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

    @ApiOperation(value = "Add a rant to the database")
    @PostMapping(path = "/rants")
    String createRant(Rant rant){
        if (rant == null)
            return failure;
        rantRepository.save(rant);
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

    @ApiOperation(value = "Increment like for rant at given id")
    @PutMapping("/rants/{id}/like")
    Rant likeRant(@PathVariable int id){
        Rant rant = rantRepository.findById(id);
        if(rant == null)
            return null;
        rant.addLike();
        rantRepository.save(rant);
        return rantRepository.findById(id);
    }

    @ApiOperation(value = "Increment dislike for rant at given id")
    @PutMapping("/rants/{id}/dislike")
    Rant dislikeRant(@PathVariable int id){
        Rant rant = rantRepository.findById(id);
        if(rant == null)
            return null;
        rant.addDislike();
        rantRepository.save(rant);
        return rantRepository.findById(id);
    }
}
