package de.fh.dortmund.fakedata.destroyer.post;

import de.fh.dortmund.helper.Timer;
import de.fh.dortmund.models.Answer;
import de.fh.dortmund.models.Question;

import java.util.List;
import java.util.Random;

public class PostDestroyer {

    static Timer timer = new Timer();
    static Random random = new Random();

    public static long destroyQuestions(List<Question> questionsRef, List<Answer> answersRef, int amount, boolean debug) {

        if(questionsRef.isEmpty() || questionsRef.size() < amount) {
            System.out.println("No questions found or amount to big. Please create questions first.");
            return -1;
        }

        timer.start();

        for (int i = 0; i < amount; i++) {
            int indexToRemove = random.nextInt(questionsRef.size());
            Question victim = questionsRef.remove(indexToRemove);
            //removePost(victim);

            for (int j = 0; j < answersRef.size(); j++) {

                if(answersRef.get(j).getParentPostId().equals(victim.getId())) {
                    Answer answer = answersRef.remove(j);
                    //removePost(answer);
                }
            }

        }

        long timeToDestroy = timer.getElapsedTime();

        if(debug) {
            System.out.println("Removed " + amount + " questions and their answers in " + timeToDestroy + " ms");
        }

        return timeToDestroy;

    }

    public static long destroyAnswers(List<Answer> answersRef, int amount, boolean debug) {

        if(answersRef.isEmpty() || answersRef.size() < amount) {
            System.out.println("No answers found or amount to big. Please create answers first.");
            return -1;
        }

        timer.start();

        for (int i = 0; i < amount; i++) {
            int indexToRemove = random.nextInt(answersRef.size());
            Answer victim = answersRef.remove(indexToRemove);
            //removePost(victim);
        }

        long timeToDestroy = timer.getElapsedTime();

        if(debug) {
            System.out.println("Removed " + amount + " answers in " + timeToDestroy + " ms");
        }

        return timeToDestroy;

    }

}

