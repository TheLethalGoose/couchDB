package de.fh.dortmund.fakedata.generator.post;

import com.github.javafaker.Faker;
import de.fh.dortmund.helper.LocalDateTimeGenerator;
import de.fh.dortmund.helper.Timer;
import de.fh.dortmund.models.Question;
import de.fh.dortmund.models.Tag;
import de.fh.dortmund.models.User;
import de.fh.dortmund.service.POST;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class QuestionGenerator {

    static Faker faker = new Faker();
    static Timer timer = new Timer();
    static Random random = new Random();

    public static List<Question> generateQuestions(List<User> users, int amount, int tagAmount, List<Tag> allTags) {
        List<Question> questions = new ArrayList<>();

        if(users.isEmpty()){
            System.out.println("No users found. Please create users first.");
            return null;
        }

        Collections.shuffle(allTags); // Shuffle once before loop
        int tagIndex = 0;

        for (int i = 0; i < amount; i++) {
            int randomUserIndex = (int)(Math.random() * users.size());

            String title = faker.lorem().sentence();
            String body = faker.lorem().paragraph();
            String userId = users.get(randomUserIndex).getId();
            int views = faker.number().numberBetween(0, 10000);

            List<Tag> tags = new ArrayList<>();
            for (int j = 0; j < tagAmount; j++) {
                tags.add(allTags.get(tagIndex));
                tagIndex = (tagIndex + 1) % allTags.size(); // Rotate index
            }

            LocalDateTime createdAt = LocalDateTimeGenerator.generateRandomLocalDateTime();
            LocalDateTime modifiedAt = LocalDateTimeGenerator.generateRandomLocalDateTimeAfter(createdAt);

            Question newQuestion = new Question(userId, title, body, createdAt.toString(), modifiedAt.toString(), views, tags);
            questions.add(newQuestion);
        }

        return questions;
    }
}
