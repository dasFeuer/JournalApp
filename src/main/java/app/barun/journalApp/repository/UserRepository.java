package app.barun.journalApp.repository;

import app.barun.journalApp.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository  extends MongoRepository<User, ObjectId> {
    User findByUserName(String username);
}
