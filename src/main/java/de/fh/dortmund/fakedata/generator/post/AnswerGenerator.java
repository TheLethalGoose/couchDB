package de.fh.dortmund.fakedata.generator.post;

import com.github.javafaker.Faker;
import de.fh.dortmund.helper.LocalDateTimeGenerator;
import de.fh.dortmund.helper.Timer;
import de.fh.dortmund.models.Answer;
import de.fh.dortmund.models.Question;
import de.fh.dortmund.models.User;
import de.fh.dortmund.service.POST;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnswerGenerator {

    static Faker faker = new Faker();
    static Timer timer = new Timer();
    static Random random = new Random();

    public static List<Answer> generateAnswers(List<Question> questions, List<User> users, int amount) {
        List<Answer> answers = new ArrayList<>();

        if (users.isEmpty() || questions.isEmpty()) {
            System.out.println("No users or questions found. Please create users and questions first.");
            return null;
        }

        for (int i = 0; i < amount; i++) {

            int randomQuestionIndex = (int) (Math.random() * questions.size());
            int randomUserIndex = (int)(Math.random() * users.size());

            String answerText = faker.lorem().sentence();
            User user = users.get(randomUserIndex);
            Question question = questions.get(randomQuestionIndex);

            boolean accepted = faker.bool().bool();

            LocalDateTime createdAt = LocalDateTimeGenerator.generateRandomLocalDateTimeAfter(LocalDateTime.parse(question.getCreatedAt()));
            LocalDateTime modifiedAt = LocalDateTimeGenerator.generateRandomLocalDateTimeAfter(createdAt);

            Answer newAnswer = new Answer(user.getId(), question.getId(), answerText, accepted, createdAt.toString(), modifiedAt.toString());
            answers.add(newAnswer);

        }

        return answers;
    }
}
