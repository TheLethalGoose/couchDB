package de.fh.dortmund.fakedata.editor;

import com.github.javafaker.Faker;
import de.fh.dortmund.helper.Timer;
import de.fh.dortmund.models.Answer;
import de.fh.dortmund.models.Question;
import de.fh.dortmund.models.User;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Random;

import static de.fh.dortmund.helper.Statistics.calculateMedian;
import static de.fh.dortmund.service.PUT.*;

public class PostEditor {
    static Timer timer = new Timer();
    static Random random = new Random();

    static Faker faker = new Faker();

    @SneakyThrows
    public static long medianTimeToAcceptAnswer(List<Answer> answers, int iterations){
        long[] times = new long[iterations];

        for(int i = 0; i < iterations; i++){
            Answer answer = answers.get(random.nextInt(answers.size()));
            if(answer.isAccepted()) {
                continue;
            }
            timer.start();
            markAnswerAsAccepted(answer);
            long time = timer.getElapsedTime();
            times[i] = time;
        }
        return calculateMedian(times);
    }

    @SneakyThrows
    public static long medianTimeToAddQuestionToFavorites(List<User> users, List<Question> questions, int iterations){
        long[] times = new long[iterations];

        User randomUser = users.get(random.nextInt(users.size()));

        for(int i = 0; i < iterations; i++){
            Question question = questions.get(random.nextInt(questions.size()));
            timer.start();
            addPostToFavorites(randomUser, question);
            long time = timer.getElapsedTime();
            times[i] = time;
        }
        return calculateMedian(times);
    }

    @SneakyThrows
    public static long medianTimeToEditQuestion(List<Question> questions, int iterations) {
        long[] times = new long[iterations];

        for(int i = 0; i < iterations; i++){
            Question question = questions.get(random.nextInt(questions.size()));
            timer.start();
            updatePost(question, faker.lorem().paragraph());
            long time = timer.getElapsedTime();
            times[i] = time;
        }
        return calculateMedian(times);

    }
}
