package app.barun.journalApp.controller;

import app.barun.journalApp.entity.User;
import app.barun.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "OK!";
    }

    @PostMapping("/create-user")
    public void createUser(@RequestBody User user) {
        userService.saveNewUser(user);
    }

//
//    @PostMapping("/create-user")
//    public ResponseEntity<String> createUser(@RequestBody User user) {
//        try {
//            userService.saveNewUser(user);
//            return new ResponseEntity<>("User created successfully!", HttpStatus.CREATED);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>("ERROR:'" + e.getMessage(), HttpStatus.CONFLICT);
//        }
//    }
}
