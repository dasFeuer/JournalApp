package app.barun.journalApp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HomeController {

    @GetMapping("/")
    @Operation(summary = "Health Check", description = "Endpoint to verify that the application is running and healthy.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Application is running and healthy.")
    })
    public String healthCheck() {
        log.info("Health check endpoint was accessed. Health is okay!");
        return "Home!";
    }
}
