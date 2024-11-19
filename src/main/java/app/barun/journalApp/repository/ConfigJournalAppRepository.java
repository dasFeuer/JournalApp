package app.barun.journalApp.repository;

import app.barun.journalApp.entity.ConfigJournalAppEntity;
import app.barun.journalApp.entity.Journal;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigJournalAppRepository extends MongoRepository<ConfigJournalAppEntity, ObjectId>{

}
