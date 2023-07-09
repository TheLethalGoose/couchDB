package de.fh.dortmund.fakedata.editor;

import com.github.javafaker.Faker;
import de.fh.dortmund.helper.Timer;
import de.fh.dortmund.models.Answer;
import de.fh.dortmund.models.Question;
import de.fh.dortmund.models.User;
import de.fh.dortmund.models.enums.ModerationTag;
import lombok.SneakyThrows;

import java.util.HashSet;
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
    public static long medianTimeToEditQuestion(List<Question> questions, int iterations, int questionsToAlter) {
        long[] times = new long[iterations];

        for(int i = 0; i < iterations; i++){
            timer.start();

            for(int j = 0; j<questionsToAlter; j++){
                Question question = questions.get(random.nextInt(questions.size()));
                updatePost(question, faker.lorem().paragraph());
            }

            long time = timer.getElapsedTime();
            times[i] = time;
        }
        return calculateMedian(times);
    }

    @SneakyThrows
    public static long medianTimeToModerateQuestions(List<Question> questions, int iterations) {
        long[] times = new long[iterations];

        for(int i = 0; i < iterations; i++){
            Question question = questions.get(random.nextInt(questions.size()));
            String moderationReason = faker.lorem().paragraph();

            HashSet<ModerationTag> moderationTags = new HashSet<>();
            int moderationTagCount = random.nextInt(1,3);

            for(int j = 0; j < moderationTagCount; j++){
                moderationTags.add(ModerationTag.fromValue(random.nextInt(4)));
            }

            ModerationTag[] moderationTagArray = moderationTags.toArray(new ModerationTag[0]);

            timer.start();
            moderateQuestion(question, moderationTagArray, moderationReason);
            long time = timer.getElapsedTime();
            times[i] = time;
        }
        return calculateMedian(times);

    }
}
