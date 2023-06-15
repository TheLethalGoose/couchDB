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

    public static long generateQuestions(String databaseName, List<Question> questionsRef, List<User> usersRef, Set<Tag> tagsRef, int amount, int tagAmount, boolean debug) {

        if(usersRef.isEmpty()){
            System.out.println("No users found. Please create users first.");
            return -1;
        }

        POST POST = new POST(databaseName);

        timer.start();

        for (int i = 0; i < amount; i++) {

            int tagsToGenerate = random.nextInt(tagAmount) + 1;
            int randomUserIndex = (int)(Math.random() * usersRef.size());

            String title = faker.lorem().sentence();
            String body = faker.lorem().paragraph();
            String userId = usersRef.get(randomUserIndex).getId();
            int views = faker.number().numberBetween(0, 10000);

            LocalDateTime createdAt = LocalDateTimeGenerator.generateRandomLocalDateTime();
            LocalDateTime modifiedAt = LocalDateTimeGenerator.generateRandomLocalDateTimeAfter(createdAt);

            for(int tagCount = 0; tagCount < tagsToGenerate; tagCount++) {

                String tagName = faker.hacker().noun();
                String tagInfo = faker.lorem().sentence();
                Tag newTag = new Tag(tagName, tagInfo);

                tagsRef.add(newTag);
            }

            Question newQuestion = new Question(userId, title, body, createdAt.toString(), modifiedAt.toString(), views);
            questionsRef.add(newQuestion);
        }

        try {
            POST.post(questionsRef);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        long timeToCreate = timer.getElapsedTime();

        if (debug){
            System.out.println("Created " + amount + " questions in " + timeToCreate + " ms.");
        }

        return timer.getElapsedTime();

    }

}
