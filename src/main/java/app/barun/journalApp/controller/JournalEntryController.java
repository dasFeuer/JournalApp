package app.barun.journalApp.controller;

import app.barun.journalApp.model.Journal;
import app.barun.journalApp.model.User;
import app.barun.journalApp.service.JournalService;
import app.barun.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalService journalService;

    @Autowired
    private UserService userService;

    @GetMapping("/{userName}")
    public ResponseEntity<?> getAllJournalOfUser(@PathVariable String userName) {
        User user = userService.findByUserName(userName);
        List<Journal> journal = user.getJournal();
        if (journal != null && !journal.isEmpty()){
            return new ResponseEntity<>(journal, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<Journal> getJournalEntryById(@PathVariable ObjectId myId) {
        Optional<Journal> journal = journalService.findById(myId);
        if(journal.isPresent()) {
            return new ResponseEntity<>(journal.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // At one line with lambda expression: return journal.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
    }

    @PostMapping("{userName}")
    public ResponseEntity<Journal> createEntry(@RequestBody Journal journal, @PathVariable String userName){
        try{
            journalService.saveEntry(journal, userName);
            return new ResponseEntity<>(journal, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("id/{userName}/{myId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myId, @PathVariable String userName){
         journalService.deleteById(myId, userName);
         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("id/{userName}/{myId}")
    public ResponseEntity<?> updateJournalById(
            @PathVariable ObjectId myId,
            @RequestBody Journal newJournal,
            @PathVariable String userName
    ) {
        Journal old = journalService.findById(myId).orElse(null);
        if (old != null) {
            old.setTitle(newJournal.getTitle() != null && !newJournal.getTitle().equals("") ? newJournal.getTitle() : old.getTitle());
            old.setContent(newJournal.getContent() != null && !newJournal.getContent().equals("") ? newJournal.getContent() : old.getContent());
            journalService.saveEntry(old);
            return new ResponseEntity<>(newJournal, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    }

