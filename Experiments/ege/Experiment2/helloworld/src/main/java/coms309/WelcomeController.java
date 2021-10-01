package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome1() {

        return "I don't know what my name is :(";
    }

    @GetMapping("/{name}")
    public String welcome2(@PathVariable String name) {

        return "Oh, so my name is " + name + ". But what is my age?";
    }

    @GetMapping("/{name}/{number}")
    public String welcome3(@PathVariable String name,@PathVariable String number) {

        return "Ahhhh, I'm " + name + " and I'm " + number + " years old.";
    }
}
