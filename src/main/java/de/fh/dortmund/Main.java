package de.fh.dortmund;

import de.fh.dortmund.couchDB.couchInitializer;
import de.fh.dortmund.fakedata.generator.post.AnswerGenerator;
import de.fh.dortmund.fakedata.generator.post.CommentGenerator;
import de.fh.dortmund.fakedata.generator.post.QuestionGenerator;
import de.fh.dortmund.fakedata.generator.user.UserGenerator;
import de.fh.dortmund.fakedata.generator.vote.VoteGenerator;
import de.fh.dortmund.models.*;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Main {

    @SneakyThrows
    public static void main(String[] args) {

        couchInitializer.init("localhost", 5984, "stackoverflow", "admin", "admin", true);

        List<User> users = new ArrayList<>();
        List<Question> questions = new ArrayList<>();
        List<Answer> answers = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        List<Vote> votes = new ArrayList<>();

        HashSet<Tag> tags = new HashSet<>();

        UserGenerator.generateUsers("stackoverflow", users, 1000, true);
        QuestionGenerator.generateQuestions("stackoverflow", questions, users, tags, 1000, 5, true);
        AnswerGenerator.generateAnswers("stackoverflow", answers, questions, users, 1000, true);
        CommentGenerator.generateComments("stackoverflow", comments, questions, users, 1000, true);
        VoteGenerator.generateVotes("stackoverflow", votes, comments, questions, answers, users, true);
    }
}