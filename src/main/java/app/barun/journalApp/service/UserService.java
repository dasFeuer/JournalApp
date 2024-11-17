package app.barun.journalApp.service;


import app.barun.journalApp.model.User;
import app.barun.journalApp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

//    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

//    public void saveEntry(User user) {
//        if (userRepository.findByUserName(user.getUserName()) != null) {
//            throw new IllegalArgumentException("Username '" + user.getUserName() + "' already exists.");
//        }
//        userRepository.save(user);
//    }

    public boolean saveNewUser(User user) {
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
            return true;
        } catch (Exception e){
//            e.printStackTrace(); // Logs the error
            log.error("Error occurred");
            log.warn("Error occurred");
            log.info("Error occurred");
            log.debug("Error occurred");
            log.trace("Error occurred");
            return false;
        }
    }

    public void saveAdminUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER", "ADMIN"));
        userRepository.save(user);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(ObjectId id) {
        return userRepository.findById(id);
    }

    public void deleteById(ObjectId id) {
        userRepository.deleteById(id);
    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

}
