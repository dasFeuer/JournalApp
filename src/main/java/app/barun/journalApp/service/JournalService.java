package app.barun.journalApp.service;

import app.barun.journalApp.model.Journal;
import app.barun.journalApp.model.User;
import app.barun.journalApp.repository.JournalRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalService {

    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private UserService userService;

    public void saveEntry(Journal journal, String userName){
        User user = userService.findByUserName(userName);
        journal.setDate(LocalDateTime.now());
        Journal saved = journalRepository.save(journal);
        user.getJournal().add(saved);
        userService.saveEntry(user);
    }

    public void saveEntry(Journal journal){
        journalRepository.save(journal);
    }

    public List<Journal> getAll(){
        return journalRepository.findAll();
    }

    public Optional<Journal> findById(ObjectId id){
        return journalRepository.findById(id);
    }

    public void deleteById(ObjectId id, String userName){
        User user = userService.findByUserName(userName);
        user.getJournal().removeIf(x -> x.getId().equals(id));
        userService.saveEntry(user);
        journalRepository.deleteById(id);
    }
}
