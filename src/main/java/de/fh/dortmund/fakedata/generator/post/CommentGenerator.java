package de.fh.dortmund.fakedata.generator.post;

import com.github.javafaker.Faker;
import de.fh.dortmund.helper.LocalDateTimeGenerator;
import de.fh.dortmund.helper.Timer;
import de.fh.dortmund.models.Comment;
import de.fh.dortmund.models.Question;
import de.fh.dortmund.models.User;
import de.fh.dortmund.service.POST;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class CommentGenerator {

    static Faker faker = new Faker();
    static Timer timer = new Timer();

    public static long generateComments(String databaseName, List<Comment> commentsRef, List<Question> questionsRef, List<User> usersRef, int amount, boolean debug) {

        if (usersRef.isEmpty() || questionsRef.isEmpty()) {
            System.out.println("No users or questions found. Please create users and questions first.");
            return -1;
        }

        POST POST = new POST(databaseName);

        timer.start();

        for (int i = 0; i < amount; i++) {

            int randomQuestionIndex = (int) (Math.random() * questionsRef.size());
            int randomUserIndex = (int)(Math.random() * usersRef.size());

            String commentText = faker.lorem().sentence();
            User user = usersRef.get(randomUserIndex);
            Question question = questionsRef.get(randomQuestionIndex);

            LocalDateTime createdAt = LocalDateTimeGenerator.generateRandomLocalDateTimeAfter(LocalDateTime.parse(question.getCreatedAt()));
            LocalDateTime modifiedAt = LocalDateTimeGenerator.generateRandomLocalDateTimeAfter(createdAt);

            Comment newComment = new Comment(user.getId(), question.getId(), commentText, createdAt.toString(), modifiedAt.toString());
            commentsRef.add(newComment);

        }

        try {
            POST.post(commentsRef);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        long timeToCreate = timer.getElapsedTime();

        if (debug){
            System.out.println("Created " + amount + " comments in " + timeToCreate + " ms.");
        }

        return timer.getElapsedTime();

    }

}
