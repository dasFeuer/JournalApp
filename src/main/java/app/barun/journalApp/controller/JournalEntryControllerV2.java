package app.barun.journalApp.controller;

import app.barun.journalApp.model.Journal;
import app.barun.journalApp.service.JournalService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;


@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalService journalService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Journal> journal = journalService.getAll();
        if (journal != null && !journal.isEmpty()){
            return new ResponseEntity<>(journal, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/id/{myId}")
    public ResponseEntity<Journal> getJournalEntryById(@PathVariable ObjectId myId) {
        Optional<Journal> journal = journalService.findById(myId);
        if(journal.isPresent()) {
            return new ResponseEntity<>(journal.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // At one line with lambda expression: return journal.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
    }

    @PostMapping
    public ResponseEntity<Journal> createEntry(@RequestBody Journal journal){
        try{
            journal.setDate(LocalDateTime.now());
            journalService.saveEntry(journal);
            return new ResponseEntity<>(journal, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myId){
         journalService.deleteById(myId);
         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<?> updateJournalById(@PathVariable ObjectId id, @RequestBody Journal newJournal) {
        Journal old = journalService.findById(id).orElse(null);
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
