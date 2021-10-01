package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Hello there! This is an example of Springboot for Damandeep Riat. Please try loading with a name/age ";
    }

    @GetMapping("/{name}/{age}")
    public String welcome(@PathVariable String name,@PathVariable int age) {
        return "My name is " + name +" and I am "+ age +" years old.";
    }


}
