package app.barun.journalApp.controller;

import app.barun.journalApp.model.Journal;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    private Map<Long, Journal> journalEntries = new HashMap<>();

    @GetMapping
    public List<Journal> getAll() {
        return new ArrayList<>(journalEntries.values());
    }

    @GetMapping("/id/{myId}")
    public Journal getJournalEntryById(@PathVariable Long myId) {
        return journalEntries.get(myId);
    }

    @PostMapping
    public boolean createEntry(@RequestBody Journal journal){
        journalEntries.put(journal.getId(), journal);
        return true;
    }

    @DeleteMapping("id/{myId}")
    public Journal deleteJournalEntryById(@PathVariable Long myId){
        return journalEntries.remove(myId);
    }

    @PutMapping("/id/{id}")
    public Journal updateJournalById(@PathVariable Long id, @RequestBody Journal journal){
        return journalEntries.put(id, journal);
    }
}
