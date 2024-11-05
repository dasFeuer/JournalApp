package app.barun.journalApp.controller;

import app.barun.journalApp.model.Journal;
import app.barun.journalApp.service.JournalService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;


@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalService journalService;

    @GetMapping
    public List<Journal> getAll() {
        return journalService.getAll();
    }

    @GetMapping("/id/{myId}")
    public Journal getJournalEntryById(@PathVariable ObjectId myId) {
        return journalService.findById(myId).orElse(null);
    }

    @PostMapping
    public Journal createEntry(@RequestBody Journal journal){
        journal.setDate(LocalDateTime.now());
        journalService.saveEntry(journal);
        return journal;
    }

    @DeleteMapping("id/{myId}")
    public boolean deleteJournalEntryById(@PathVariable ObjectId myId){
         journalService.deleteById(myId);
         return true;
    }

    @PutMapping("/id/{id}")
    public Journal updateJournalById(@PathVariable ObjectId id, @RequestBody Journal newJournal){
        Journal old = journalService.findById(id).orElse(null);
        if(old != null) {
            old.setTitle(newJournal.getTitle() != null && !newJournal.getTitle().equals("") ? newJournal.getTitle() : old.getTitle());
            old.setContent(newJournal.getContent() != null && !newJournal.getContent().equals("") ? newJournal.getContent() : old.getContent());
        }
        journalService.saveEntry(old);
        return old;
    }
}
