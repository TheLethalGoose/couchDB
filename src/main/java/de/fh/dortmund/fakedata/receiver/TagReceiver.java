package de.fh.dortmund.fakedata.receiver;

import de.fh.dortmund.helper.Timer;

import java.util.Random;

import static de.fh.dortmund.helper.Statistics.calculateMedian;
import static de.fh.dortmund.service.GET.getDocsFromView;

public class TagReceiver {
    static Timer timer = new Timer();
    static Random random = new Random();

    public static long medianTimeToFetchAllTags(int iterations) {
        long[] times = new long[iterations];

        for(int i = 0; i < iterations; i++){
            timer.start();
            getDocsFromView("_design/tags","allTags",true, true);
            long time = timer.getElapsedTime();
            times[i] = time;
        }
        return calculateMedian(times);
    }

    public static long medianTimeToFetchPopularTags(int iterations) {
        long[] times = new long[iterations];

        for(int i = 0; i < iterations; i++){
            timer.start();
            getDocsFromView("_design/tags","popularTags",true, true);
            long time = timer.getElapsedTime();
            times[i] = time;
        }
        return calculateMedian(times);
    }
}
