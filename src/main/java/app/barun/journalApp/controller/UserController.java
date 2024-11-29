package app.barun.journalApp.controller;

import app.barun.journalApp.api.Response.WeatherResponse;
import app.barun.journalApp.entity.User;
import app.barun.journalApp.repository.UserRepository;
import app.barun.journalApp.service.UserService;
import app.barun.journalApp.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "User APIs", description = "APIs for managing user information such as updating, deleting, and personalized greetings.")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WeatherService weatherService;

    @PutMapping
    @Operation(summary = "Update user", description = "Update the username and password of the currently authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid user details provided."),
            @ApiResponse(responseCode = "401", description = "User not authenticated.")
    })
    public ResponseEntity<Void> updateUser(
            @Parameter(description = "User object containing updated username and password.") @RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User userInDb = userService.findByUserName(userName);

        userInDb.setUserName(user.getUserName());
        userInDb.setPassword(user.getPassword());

        userService.saveNewUser(userInDb);
        return ResponseEntity.ok().build();
    }


    @GetMapping
    @Operation(summary = "Greet user with weather info", description = "Provide a personalized greeting to the currently authenticated user, along with current weather conditions.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Greeting and weather info provided successfully."),
            @ApiResponse(responseCode = "401", description = "User not authenticated."),
            @ApiResponse(responseCode = "500", description = "Unable to fetch weather information.")
    })
    public ResponseEntity<String> greeting() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        String greetingMessage = "Hi " + userName;

        try {
            WeatherResponse weatherResponse = weatherService.getWeather("Kathmandu");
            if (weatherResponse != null) {
                greetingMessage += ", Weather feels like " + weatherResponse.getCurrent().getFeelslike();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Hi " + userName + ". Unable to fetch weather information at this time.");
        }

        return ResponseEntity.ok(greetingMessage);
    }
}
