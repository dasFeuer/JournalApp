package net.engineeringdigest.journalApp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
public class MyClass {
    @GetMapping
    public String greet() {
        return "Welcome! Hi, have some Spring!";
    }
}
