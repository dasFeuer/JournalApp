package app.barun.journalApp.model;

import lombok.Data;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "journal")
@Data
public class Journal {

    @Id
    private ObjectId id;

    @NonNull
    private String title;

    private String content;

    private LocalDateTime date;

}
