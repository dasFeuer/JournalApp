package app.barun.journalApp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;


    @Test
    void testSendMail(){
        emailService.sendEmail(
                "barunpanthisharma11@gmail.com",
                "Testing Java email sender",
                "It's working smoothly");
    }
}
