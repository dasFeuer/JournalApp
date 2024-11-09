package app.barun.journalApp.repository;

import app.barun.journalApp.model.Journal;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JournalRepository extends MongoRepository<Journal, ObjectId>{

}
