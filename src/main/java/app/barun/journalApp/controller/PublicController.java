package app.barun.journalApp.controller;

import app.barun.journalApp.dto.UserDTO;
import app.barun.journalApp.entity.User;
import app.barun.journalApp.service.UserDetailsServiceImpl;
import app.barun.journalApp.service.UserService;
import app.barun.journalApp.utilis.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Public APIs", description = "APIs for user authentication and registration.")
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;


    /*
    For all method, first login should be done for JwtToken
     */
    @PostMapping("/signup")
    @Operation(summary = "Register a new user", description = "Sign up a new user by providing their details.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully."),
            @ApiResponse(responseCode = "409", description = "User with the provided details already exists."),
            @ApiResponse(responseCode = "400", description = "Invalid user input.")
    })
    public ResponseEntity<String> signup(@RequestBody UserDTO userDTO) {
        try {
            if (userDTO.getEmail() == null || userDTO.getUserName() == null || userDTO.getPassword() == null) {
                return new ResponseEntity<>("Invalid input: username, email, and password are required.", HttpStatus.BAD_REQUEST);
            }

            if (userService.findByUserName(userDTO.getUserName()) != null) {
                return new ResponseEntity<>("Username already exists.", HttpStatus.CONFLICT);
            }

            User newUser = new User();
            newUser.setEmail(userDTO.getEmail());
            newUser.setUserName(userDTO.getUserName());
            newUser.setPassword(userDTO.getPassword());
            newUser.setSentimentAnalysis(userDTO.isSentimentAnalysis());

            userService.saveNewUser(newUser);

            return new ResponseEntity<>("User created successfully!", HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error during signup: {}", e.getMessage(), e);
            return new ResponseEntity<>("Unexpected error during signup. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate a user and generate a JWT token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User authenticated successfully, token generated."),
            @ApiResponse(responseCode = "400", description = "Invalid username or password."),
            @ApiResponse(responseCode = "500", description = "Unexpected error occurred.")
    })
    public ResponseEntity<String> login(@RequestBody User user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUserName(),
                            user.getPassword()
                    )
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());

            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error during login: {}", e.getMessage());
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }
}
