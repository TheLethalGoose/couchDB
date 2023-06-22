package de.fh.dortmund.fakedata.generator.vote;

import de.fh.dortmund.helper.Timer;
import de.fh.dortmund.models.*;
import de.fh.dortmund.service.POST;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VoteGenerator {

    static Random random = new Random();
    static Timer timer = new Timer();

    public static List<Vote> generateVotes(List<Comment> comments, List<Question> questions, List<Answer> answers, List<User> users) {
        List<Vote> votes = new ArrayList<>();

        if (users.isEmpty() || questions.isEmpty() || answers.isEmpty() || comments.isEmpty()) {
            System.out.println("No users, answers, comments or questions found. Please create them first.");
            return null;
        }

        for (User user : users) {

            int randomQuestionIndex = (int) (Math.random() * questions.size());
            int randomAnswerIndex = (int)(Math.random() * answers.size());
            int randomCommentIndex = (int)(Math.random() * comments.size());

            Vote newQuestionVote = new Vote(user.getId(), questions.get(randomQuestionIndex).getId(), Vote.randomVoteType());
            Vote newCommentVote = new Vote(user.getId(), comments.get(randomCommentIndex).getId(), Vote.randomVoteType());
            Vote newAnswerVote = new Vote(user.getId(), answers.get(randomAnswerIndex).getId(), Vote.randomVoteType());


            int numVotesToAdd = random.nextInt(4);

            if (numVotesToAdd >= 2) {
                votes.add(newQuestionVote);
            }
            if (numVotesToAdd >= 2) {
                votes.add(newCommentVote);
            }
            if (numVotesToAdd == 3) {
                votes.add(newAnswerVote);
            }

        }

        return votes;
    }
}
