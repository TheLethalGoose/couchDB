package de.fh.dortmund.fakedata.receiver;

import de.fh.dortmund.helper.Timer;
import de.fh.dortmund.models.Question;
import de.fh.dortmund.models.Tag;
import de.fh.dortmund.models.User;

import java.util.List;
import java.util.Random;

import static de.fh.dortmund.helper.Statistics.calculateMedian;
import static de.fh.dortmund.service.GET.getDocsFromView;

public class VoteReceiver {

    static Timer timer = new Timer();
    static Random random = new Random();

    public static long medianTimeToFetchAllVotes(int iterations) {
            long[] times = new long[iterations];

            for(int i = 0; i < iterations; i++){
                timer.start();
                getDocsFromView("_design/votes","allVotes");
                long time = timer.getElapsedTime();
                times[i] = time;
            }
            return calculateMedian(times);

    }

    public static long medianTimeToFetchTotalVotesToQuestion(List<Question> questions, int iterations) {
        long[] times = new long[iterations];

        for(int i = 0; i < iterations; i++){
            Question question =  questions.get(random.nextInt(questions.size()));
            timer.start();
            getDocsFromView("_design/votes","totalVotesToQuestion", question.getId(),true);
            long time = timer.getElapsedTime();
            times[i] = time;
        }
        return calculateMedian(times);
    }
}
