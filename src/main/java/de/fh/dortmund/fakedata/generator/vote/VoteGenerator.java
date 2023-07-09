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

    public static List<Vote> generateVotes(List<Comment> comments, List<Question> questions, List<Answer> answers, List<User> users, int votesToGeneratePerPost) {
        List<Vote> votes = new ArrayList<>();

        if (users.isEmpty() || questions.isEmpty() || answers.isEmpty() || comments.isEmpty()) {
            System.out.println("No users, answers, comments or questions found. Please create them first.");
            return null;
        }

        User user = users.get(random.nextInt(users.size()));

        for (int i = 0; i < votesToGeneratePerPost; i++) {
            int randomQuestionIndex = random.nextInt(questions.size());
            int randomAnswerIndex = random.nextInt(answers.size());
            int randomCommentIndex = random.nextInt(comments.size());

            Vote newQuestionVote = new Vote(user.getId(), questions.get(randomQuestionIndex).getId(), Vote.randomVoteType());
            Vote newAnswerVote = new Vote(user.getId(), answers.get(randomAnswerIndex).getId(), Vote.randomVoteType());
            Vote newCommentVote = new Vote(user.getId(), comments.get(randomCommentIndex).getId(), Vote.randomVoteType());

            votes.add(newQuestionVote);
            votes.add(newAnswerVote);
            votes.add(newCommentVote);
        }

        return votes;
    }

}
