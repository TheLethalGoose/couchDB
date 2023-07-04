package de.fh.dortmund.couchDB;

import de.fh.dortmund.helper.Timer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static de.fh.dortmund.service.POST.post;


public class CouchPersistor {

    private static final Timer timer = new Timer();

    public static long persist(List<?> objects, boolean debug) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        timer.start();

        try {
            post(objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        long timeToCreate = timer.getElapsedTime();

        if(debug) {
            System.out.println("Persisted " + objects.size() + " objects of Type " +  objects.get(1).getClass().getSimpleName() + " in " + timeToCreate + "ms.");
        }

        return timeToCreate;
    }

}
