package app.barun.journalApp.service;

import app.barun.journalApp.model.Journal;
import app.barun.journalApp.repository.JournalRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JournalService {

    @Autowired
    private JournalRepository journalRepository;

    public void saveEntry(Journal journal){
        journalRepository.save(journal);
    }

    public List<Journal> getAll(){
        return journalRepository.findAll();
    }

    public Optional<Journal> findById(ObjectId id){
        return journalRepository.findById(id);
    }

    public void deleteById(ObjectId id){
        journalRepository.deleteById(id);
    }
}
