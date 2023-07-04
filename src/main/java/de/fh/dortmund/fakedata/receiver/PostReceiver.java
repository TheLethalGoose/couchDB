package de.fh.dortmund.fakedata.receiver;

import de.fh.dortmund.helper.Timer;
import de.fh.dortmund.models.Question;
import de.fh.dortmund.models.Tag;
import de.fh.dortmund.models.User;

import java.util.List;
import java.util.Random;

import static de.fh.dortmund.helper.Statistics.calculateMedian;
import static de.fh.dortmund.service.GET.getDocsFromView;

public class PostReceiver {

    static Timer timer = new Timer();
    static Random random = new Random();

    public static long medianTimeToFetchAnswersByQuestion(List<Question> questions, int iterations){
        long[] times = new long[iterations];

        for(int i = 0; i < iterations; i++){
            Question question = questions.get(random.nextInt(questions.size()));
            timer.start();
            getDocsFromView("_design/posts","answersToQuestion", question.getId());
            long time = timer.getElapsedTime();
            times[i] = time;
        }
        return calculateMedian(times);
    }

    public static long medianTimeToFetchQuestionsByUserId(List<User> users, int iterations){
        long[] times = new long[iterations];


        for(int i = 0; i < iterations; i++){
            User user = users.get(random.nextInt(users.size()));
            timer.start();
            getDocsFromView("_design/posts","byUserId", user.getId());
            long time = timer.getElapsedTime();
            times[i] = time;
        }
        return calculateMedian(times);
    }

    public static long medianTimeToFetchLatestQuestions(int iterations) {
        long[] times = new long[iterations];

        for (int i = 0; i < iterations; i++) {
            timer.start();
            getDocsFromView("_design/posts","latestQuestions");
            long time = timer.getElapsedTime();
            times[i] = time;
        }
        return calculateMedian(times);
    }

    public static long medianTimeToFetchAllQuestions(int iterations) {
        long[] times = new long[iterations];
        for (int i = 0; i < iterations; i++) {
            timer.start();
            getDocsFromView("_design/posts","allQuestions");
            long time = timer.getElapsedTime();
            times[i] = time;
        }
        return calculateMedian(times);
    }

    public static long medianTimeToFetchAllAnswers(int iterations) {
        long[] times = new long[iterations];

        for (int i = 0; i < iterations; i++) {
            timer.start();
            getDocsFromView("_design/posts","allAnswers");
            long time = timer.getElapsedTime();
            times[i] = time;
        }
        return calculateMedian(times);
    }

    public static long medianTimeToFetchAllComments(int iterations) {
        long[] times = new long[iterations];

        for (int i = 0; i < iterations; i++) {
            timer.start();
            getDocsFromView("_design/posts","allComments");
            long time = timer.getElapsedTime();
            times[i] = time;
        }
        return calculateMedian(times);
    }

    public static long medianTimeToFetchAllPosts(int iterations) {
        long[] times = new long[iterations];

        for (int i = 0; i < iterations; i++) {
            timer.start();
            getDocsFromView("_design/posts","allComments");
            long time = timer.getElapsedTime();
            times[i] = time;
        }
        return calculateMedian(times);
    }

    public static long medianTimeToFetchPostsByTagName(List<Tag> tags, int iterations){
        long[] times = new long[iterations];

        for(int i = 0; i < iterations; i++){
            Tag tag = tags.get(random.nextInt(tags.size()));
            timer.start();
            getDocsFromView("_design/posts","byTagName", tag.getName());
            long time = timer.getElapsedTime();
            times[i] = time;
        }
        return calculateMedian(times);
    }

}
