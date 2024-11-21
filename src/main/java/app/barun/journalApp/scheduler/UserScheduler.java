package app.barun.journalApp.scheduler;


import app.barun.journalApp.cache.AppCache;
import app.barun.journalApp.entity.Journal;
import app.barun.journalApp.entity.User;
import app.barun.journalApp.repository.UserRepositoryImpl;
import app.barun.journalApp.service.EmailService;
import app.barun.journalApp.service.SentimentAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;

    @Autowired
    private AppCache appCache;

    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUserAndSendSaMail(){

        List<User> users = userRepository.getUserForSA();
        for(User user: users){
            List<Journal> journal = user.getJournal();
            List<String> filteredEntries = journal.stream()
                    .filter(x -> x.getDate()
                            .isAfter(LocalDateTime.now()
                                    .minus(7, ChronoUnit.DAYS))).map(x -> x.getContent())
                    .collect(Collectors.toList());
            String entry = String.join(" ", filteredEntries);
            String sentiment = sentimentAnalysisService.getSentiment(entry);
            emailService.sendEmail(user.getEmail(), "Sentiment for last 7 days", sentiment);
        }
    }

    @Scheduled(cron = "0 0/10 * ? * *")
    public void clearAppCache(){
        appCache.init();
    }
}
