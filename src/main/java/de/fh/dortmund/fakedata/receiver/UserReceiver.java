package de.fh.dortmund.fakedata.receiver;

import de.fh.dortmund.helper.Timer;
import de.fh.dortmund.models.User;
import de.fh.dortmund.service.GET;

import java.util.List;
import java.util.Random;

import static de.fh.dortmund.helper.Statistics.calculateMedian;
import static de.fh.dortmund.service.GET.getDocsFromView;

public class UserReceiver {

    static Timer timer = new Timer();
    static Random random = new Random();

    public static long medianTimeToFetchUsersByEmail(List<User> users, int iterations){
        long[] times = new long[iterations];

        for(int i = 0; i < iterations; i++){
            User user = users.get(random.nextInt(users.size()));
            timer.start();
            getDocsFromView("_design/users","byEmail", user.getEmail());
            long time = timer.getElapsedTime();
            times[i] = time;
        }
        return calculateMedian(times);
    }


    public static long medianTimeToFetchAllUsers(int iterations) {
        long[] times = new long[iterations];

        for(int i = 0; i < iterations; i++){
            timer.start();
            getDocsFromView("_design/users","allUsers");
            long time = timer.getElapsedTime();
            times[i] = time;
        }
        return calculateMedian(times);
    }

    public static long medianTimeToFetchUserFavorites(List<User> users, int iterations) {
        long[] times = new long[iterations];

        for(int i = 0; i < iterations; i++){
            User user = users.get(random.nextInt(users.size()));
            timer.start();
            getDocsFromView("_design/users","userFavorites", user.getId());
            long time = timer.getElapsedTime();
            times[i] = time;
        }
        return calculateMedian(times);
    }
}
