package de.fh.dortmund.fakedata.generator.post;

import com.github.javafaker.Faker;
import de.fh.dortmund.helper.LocalDateTimeGenerator;
import de.fh.dortmund.helper.Timer;
import de.fh.dortmund.models.Comment;
import de.fh.dortmund.models.Question;
import de.fh.dortmund.models.User;
import de.fh.dortmund.service.POST;

import java.io.IOException;
import java.sql.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentGenerator {

    static Faker faker = new Faker();
    static Timer timer = new Timer();

    public static List<Comment> generateComments(List<Question> questions, List<User> users, int amount) {
        List<Comment> comments = new ArrayList<>();
        if (users.isEmpty() || questions.isEmpty()) {
            System.out.println("No users or questions found. Please create users and questions first.");
            return null;
        }

        for (int i = 0; i < amount; i++) {

            int randomQuestionIndex = (int) (Math.random() * questions.size());
            int randomUserIndex = (int)(Math.random() * users.size());

            String commentText = faker.lorem().sentence();
            User user = users.get(randomUserIndex);
            Question question = questions.get(randomQuestionIndex);

            LocalDateTime createdAt = LocalDateTimeGenerator.generateRandomLocalDateTimeAfter(LocalDateTime.parse(question.getCreatedAt()));
            LocalDateTime modifiedAt = LocalDateTimeGenerator.generateRandomLocalDateTimeAfter(createdAt);

            Comment newComment = new Comment(user.getId(), question.getId(), commentText, createdAt.toString(), modifiedAt.toString());
            comments.add(newComment);

        }

        return comments;
    }

}
