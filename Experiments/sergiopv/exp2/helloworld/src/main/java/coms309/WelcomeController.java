package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Hello random user! Have a wonderful day!";
    }

    @GetMapping("/{name}")
    public String welcome(@PathVariable String name) {
        return "Hello " + name + "! Have a wonderful day!";
    }

    @GetMapping("/{name}/{brithYear}")
    public String welcome(@PathVariable String name, @PathVariable int brithYear) {
        int age = 2021 - brithYear;
        return "Hello and welcome " + name + "! You are about " + age + " years old!";
    }
}
