package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Hello and welcome to COMS 309";
    }

    @GetMapping("/{fName}/{lName}/{id}")
    public String welcome(@PathVariable String fName, @PathVariable String lName, @PathVariable String id) {
        return "Hello and welcome to COMS 309 " +
                "| First Name: " + fName +
                "| Last Name: " + lName +
                "| ID: " + id;
    }
}
