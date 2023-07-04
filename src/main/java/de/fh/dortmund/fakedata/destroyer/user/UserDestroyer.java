package de.fh.dortmund.fakedata.destroyer.user;

import de.fh.dortmund.helper.Timer;
import de.fh.dortmund.models.User;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static de.fh.dortmund.service.DELETE.delete;

public class UserDestroyer {

    static Timer timer = new Timer();
    static Random random = new Random();

    public static long destroyUsers(List<User> usersRef, int amount, boolean debug) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        if(usersRef.isEmpty() || usersRef.size() < amount) {
            System.out.println("No users found or amount to big. Please create users first.");
            return -1;
        }

        List<User> objectsToDelete = new ArrayList<>();

        timer.start();

        for (int i = 0; i < amount; i++) {
            int indexToRemove = random.nextInt(usersRef.size());
            User victim = usersRef.remove(indexToRemove);
            objectsToDelete.add(victim);
        }

        delete(objectsToDelete);

        long timeToDestroy = timer.getElapsedTime();

        if(debug){
            System.out.println("Removed " + amount + " users in " + timeToDestroy + " ms");
        }

        return timeToDestroy;

    }
}
