package de.fh.dortmund.fakedata.generator.vote;

import de.fh.dortmund.helper.Timer;
import de.fh.dortmund.models.*;
import de.fh.dortmund.service.POST;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class VoteGenerator {

    static Random random = new Random();
    static Timer timer = new Timer();

    public static long generateVotes(String databaseName, List<Vote> votesRef, List<Comment> commentsRef, List<Question> questionsRef, List<Answer> answersRef, List<User> usersRef, boolean debug) {

        if (usersRef.isEmpty() || questionsRef.isEmpty() || answersRef.isEmpty() || commentsRef.isEmpty()) {
            System.out.println("No users, answers, comments or questions found. Please create them first.");
            return -1;
        }

        POST POST = new POST(databaseName);

        timer.start();

        for (User user : usersRef) {

            int randomQuestionIndex = (int) (Math.random() * questionsRef.size());
            int randomAnswerIndex = (int)(Math.random() * answersRef.size());
            int randomCommentIndex = (int)(Math.random() * commentsRef.size());

            Vote newQuestionVote = new Vote(user.getId(), questionsRef.get(randomQuestionIndex).getId(), Vote.randomVoteType());
            Vote newCommentVote = new Vote(user.getId(), commentsRef.get(randomCommentIndex).getId(), Vote.randomVoteType());
            Vote newAnswerVote = new Vote(user.getId(), answersRef.get(randomAnswerIndex).getId(), Vote.randomVoteType());


            int numVotesToAdd = random.nextInt(4);

            if (numVotesToAdd >= 2) {
                votesRef.add(newQuestionVote);
            }
            if (numVotesToAdd >= 2) {
                votesRef.add(newCommentVote);
            }
            if (numVotesToAdd == 3) {
                votesRef.add(newAnswerVote);
            }

        }

        try {
            POST.post(votesRef);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        long timeToCreate = timer.getElapsedTime();

        if (debug){
            System.out.println("Created " + votesRef.size() + " votes in " + timeToCreate + " ms.");
        }

        return timer.getElapsedTime();

    }



}
