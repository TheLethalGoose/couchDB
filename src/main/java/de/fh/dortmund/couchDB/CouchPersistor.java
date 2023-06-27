package de.fh.dortmund.couchDB;

import de.fh.dortmund.helper.Timer;

import java.io.IOException;
import java.util.List;

import static de.fh.dortmund.service.POST.post;


public class CouchPersistor {

    private static Timer timer = new Timer();

    public static void persist(List<?> objects, String createdObjectName, boolean debug, int amount){
        timer.start();

        try {
            post(objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        long timeToCreate = timer.getElapsedTime();

        if(debug) {
            System.out.println("Created " + amount + " " +  createdObjectName + " in " + timeToCreate + "ms.");
        }
    }

}
