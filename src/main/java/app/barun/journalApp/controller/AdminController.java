package app.barun.journalApp.controller;

import app.barun.journalApp.cache.AppCache;
import app.barun.journalApp.entity.User;
import app.barun.journalApp.service.UserService;
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

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin APIs", description = "APIs for administrative tasks like managing users and clearing caches")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AppCache appCache;

    @GetMapping("/all-users")
    @Operation(summary = "Get all users", description = "Retrieve a list of all users in the application.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of users."),
            @ApiResponse(responseCode = "404", description = "No users found.")
    })
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> all = userService.getAll();
        if (all != null && !all.isEmpty()) {
            return ResponseEntity.ok(all);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/create-admin-user")
    @Operation(summary = "Create an admin user", description = "Add a new admin user to the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Admin user created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid user details provided.")
    })
    public ResponseEntity<Void> createAdminUser(
            @Parameter(description = "User object containing admin user details.") @RequestBody User user) {
        try {
            userService.saveAdminUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/clear-app-cache")
    @Operation(summary = "Clear application cache", description = "Clear the application cache to reset cached data.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Application cache cleared successfully.")
    })
    public ResponseEntity<Void> clearAppCache() {
        appCache.init();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/id/{id}")
    @Operation(
            summary = "Delete user",
            description = "Delete a user by their ID. Requires appropriate permissions."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User deleted successfully."),
            @ApiResponse(responseCode = "404", description = "User not found."),
            @ApiResponse(responseCode = "401", description = "User not authenticated.")
    })
    public ResponseEntity<String> deleteUserById(
            @Parameter(description = "ID of the user to delete.") @PathVariable ObjectId id
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isDeleted = userService.deleteUserById(id);

        if (isDeleted) {
            return ResponseEntity.ok("User with ID " + id + " has been deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with ID " + id + " not found.");
        }
    }


}
