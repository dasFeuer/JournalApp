package app.barun.journalApp.controller;

import app.barun.journalApp.entity.User;
import app.barun.journalApp.service.UserDetailsServiceImpl;
import app.barun.journalApp.service.UserService;
import app.barun.journalApp.utilis.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    public void signup(@RequestBody User user) {
        userService.saveNewUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        try{

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUserName(),
                            user.getPassword()));

            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());


            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (Exception e){

            log.error("Exception occurred while create Authentication token ", e);
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);


        }
    }

//
//    @PostMapping("/signup")
//    public ResponseEntity<String> signup(@RequestBody User user) {
//        try {
//            userService.saveNewUser(user);
//            return new ResponseEntity<>("User created successfully!", HttpStatus.CREATED);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>("ERROR:'" + e.getMessage(), HttpStatus.CONFLICT);
//        }
//    }
}
