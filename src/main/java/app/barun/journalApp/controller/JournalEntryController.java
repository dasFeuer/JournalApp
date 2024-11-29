package app.barun.journalApp.controller;

import app.barun.journalApp.entity.Journal;
import app.barun.journalApp.entity.User;
import app.barun.journalApp.service.JournalService;
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

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
@Tag(name = "Journal APIs", description = "APIs for managing journal entries")
public class JournalEntryController {

    @Autowired
    private JournalService journalService;

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "Get all journal entries of the user", description = "Retrieve all journal entries associated with the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved journal entries."),
            @ApiResponse(responseCode = "204", description = "No journal entries found."),
            @ApiResponse(responseCode = "401", description = "Unauthorized access.")
    })
    public ResponseEntity<List<Journal>> getAllJournalOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<Journal> journals = user.getJournal();
        if (journals != null && !journals.isEmpty()) {
            return ResponseEntity.ok(journals);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("id/{myId}")
    @Operation(summary = "Get a journal entry by ID", description = "Retrieve a specific journal entry by its ID for the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the journal entry."),
            @ApiResponse(responseCode = "404", description = "Journal entry not found."),
            @ApiResponse(responseCode = "401", description = "Unauthorized access.")
    })
    public ResponseEntity<?> getJournalEntryById(
            @Parameter(description = "ID of the journal entry to retrieve.") @PathVariable String myId) {
        ObjectId objectId = new ObjectId(myId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<Journal> collect = user.getJournal().stream().filter(x -> x.getId().equals(objectId)).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            Optional<?> journal = journalService.findById(objectId);
            if (journal.isPresent()) {
                return new ResponseEntity<>(journal.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @Operation(summary = "Create a new journal entry", description = "Add a new journal entry for the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Journal entry created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid journal entry data."),
            @ApiResponse(responseCode = "401", description = "Unauthorized access.")
    })
    public ResponseEntity<Journal> createEntry(@RequestBody Journal journal) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            journalService.saveEntry(journal, userName);
            return ResponseEntity.status(HttpStatus.CREATED).body(journal);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("id/{myId}")
    @Operation(summary = "Delete a journal entry by ID", description = "Remove a specific journal entry by its ID for the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Journal entry deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Journal entry not found."),
            @ApiResponse(responseCode = "401", description = "Unauthorized access.")
    })
    public ResponseEntity<Void> deleteJournalEntryById(
            @Parameter(description = "ID of the journal entry to delete.") @PathVariable String myId) {
        ObjectId objectId = new ObjectId(myId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean removed = journalService.deleteById(objectId, userName);
        if (removed) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("id/{myId}")
    @Operation(summary = "Update a journal entry by ID", description = "Modify an existing journal entry by its ID for the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Journal entry updated successfully."),
            @ApiResponse(responseCode = "404", description = "Journal entry not found."),
            @ApiResponse(responseCode = "401", description = "Unauthorized access.")
    })
    public ResponseEntity<Journal> updateJournalById(
            @Parameter(description = "ID of the journal entry to update.") @PathVariable String myId,
            @RequestBody Journal newJournal) {
        ObjectId objectId = new ObjectId(myId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<Journal> collect = user.getJournal().stream().filter(x -> x.getId().equals(objectId)).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            Optional<Journal> journal = journalService.findById(objectId);
            if (journal.isPresent()) {
                Journal old = journal.get();
                old.setTitle(newJournal.getTitle() != null && !newJournal.getTitle().isEmpty() ? newJournal.getTitle() : old.getTitle());
                old.setContent(newJournal.getContent() != null && !newJournal.getContent().isEmpty() ? newJournal.getContent() : old.getContent());
                journalService.saveEntry(old);
                return ResponseEntity.ok(old);
            }
        }
        return ResponseEntity.notFound().build();
    }
}
