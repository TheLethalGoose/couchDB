package de.fh.dortmund.fakedata.destroyer.post;

import de.fh.dortmund.helper.Timer;
import de.fh.dortmund.models.Answer;
import de.fh.dortmund.models.Comment;
import de.fh.dortmund.models.Question;
import de.fh.dortmund.models.Vote;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static de.fh.dortmund.service.DELETE.delete;

public class PostDestroyer {

    static Timer timer = new Timer();
    static Random random = new Random();

    public static long destroyQuestions(List<Question> questionsRef, List<Answer> answersRef, List<Comment> commentsRef, List<Vote> votesRef, int amount, boolean debug) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        if(questionsRef.isEmpty() || questionsRef.size() < amount) {
            System.out.println("No questions found or amount to big. Please create questions first.");
            return -1;
        }

        List<Object> objectsToDelete = new ArrayList<>();

        timer.start();

        for (int i = 0; i < amount; i++) {
            int indexToRemove = random.nextInt(questionsRef.size());
            Question victim = questionsRef.remove(indexToRemove);
            objectsToDelete.add(victim);

            for (int j = 0; j < answersRef.size(); j++) {

                if(answersRef.get(j).getParentPostId().equals(victim.getId())) {
                    Answer answer = answersRef.remove(j);
                    objectsToDelete.add(answer);
                }
            }
            for (int j = 0; j < commentsRef.size(); j++) {

                if(commentsRef.get(j).getParentPostId().equals(victim.getId())) {
                    Comment comment = commentsRef.remove(j);
                    objectsToDelete.add(comment);
                }
            }
            for (int j = 0; j < votesRef.size(); j++) {

                if(votesRef.get(j).getPostId().equals(victim.getId())) {
                    Vote vote = votesRef.remove(j);
                    objectsToDelete.add(vote);
                }
            }
        }

        delete(objectsToDelete);

        long timeToDestroy = timer.getElapsedTime();

        if(debug) {
            System.out.println("Removed " + amount + " questions and their answers in " + timeToDestroy + " ms");
        }

        return timeToDestroy;

    }

    public static long destroyAnswers(List<Answer> answersRef, List<Vote> votesRef, int amount, boolean debug) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        if(answersRef.isEmpty() || answersRef.size() < amount) {
            System.out.println("No answers found or amount to big. Please create answers first.");
            return -1;
        }

        List<Object> objectsToDelete = new ArrayList<>();

        timer.start();

        for (int i = 0; i < amount; i++) {
            int indexToRemove = random.nextInt(answersRef.size());
            Answer victim = answersRef.remove(indexToRemove);
            objectsToDelete.add(victim);

            for (int j = 0; j < votesRef.size(); j++) {

                if(votesRef.get(j).getPostId().equals(victim.getId())) {
                    Vote vote = votesRef.remove(j);
                    objectsToDelete.add(vote);
                }
            }
        }

        delete(objectsToDelete);

        long timeToDestroy = timer.getElapsedTime();

        if(debug) {
            System.out.println("Removed " + amount + " answers in " + timeToDestroy + " ms");
        }

        return timeToDestroy;

    }

    public static long destroyComments(List<Comment> commentsRef, List<Vote> votesRef, int amount, boolean debug) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if(commentsRef.isEmpty() || commentsRef.size() < amount) {
            System.out.println("No comments found or amount to big. Please create answers first.");
            return -1;
        }

        List<Object> objectsToDelete = new ArrayList<>();

        timer.start();

        for (int i = 0; i < amount; i++) {
            int indexToRemove = random.nextInt(commentsRef.size());
            Comment victim = commentsRef.remove(indexToRemove);
            objectsToDelete.add(victim);

            for (int j = 0; j < commentsRef.size(); j++) {

                if(votesRef.get(j).getPostId().equals(victim.getId())) {
                    Vote vote = votesRef.remove(j);
                    objectsToDelete.add(vote);
                }
            }
        }

        delete(objectsToDelete);

        long timeToDestroy = timer.getElapsedTime();

        if(debug) {
            System.out.println("Removed " + amount + " answers in " + timeToDestroy + " ms");
        }

        return timeToDestroy;

    }
}

