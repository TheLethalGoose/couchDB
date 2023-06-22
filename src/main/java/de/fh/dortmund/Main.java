package de.fh.dortmund;

import de.fh.dortmund.couchDB.couchInitializer;
import de.fh.dortmund.fakedata.generator.post.AnswerGenerator;
import de.fh.dortmund.fakedata.generator.post.CommentGenerator;
import de.fh.dortmund.fakedata.generator.post.QuestionGenerator;
import de.fh.dortmund.fakedata.generator.tag.TagGenerator;
import de.fh.dortmund.fakedata.generator.user.UserGenerator;
import de.fh.dortmund.fakedata.generator.vote.VoteGenerator;
import de.fh.dortmund.helper.Timer;
import de.fh.dortmund.models.*;
import de.fh.dortmund.service.POST;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Main {
    private static final int AMOUNT = 1000;
    private static final int TAG_AMOUNT_PER_QUESTION = 5;
    private static final String DATABASE_NAME = "stackoverflow";
    private static POST POST = new POST(DATABASE_NAME);
    static Timer timer = new Timer();

    @SneakyThrows
    public static void main(String[] args) {

        couchInitializer.init("localhost", 5984, DATABASE_NAME, "admin", "admin", true);

        // Generate test data
        List<User> users = UserGenerator.generateUsers(AMOUNT);
        List<Tag> tags = TagGenerator.generateTags(AMOUNT);
        List<Question> questions = QuestionGenerator.generateQuestions(users, AMOUNT, TAG_AMOUNT_PER_QUESTION, tags);
        List<Answer> answers = AnswerGenerator.generateAnswers(questions, users, AMOUNT);
        List<Comment> comments = CommentGenerator.generateComments(questions, users, AMOUNT);
        List<Vote> votes = VoteGenerator.generateVotes(comments, questions, answers, users);

        // Insert data into database
        insert(users, "users", true, AMOUNT);
        insert(tags, "tags", true, AMOUNT);
        insert(questions, "questions", true, AMOUNT);
        insert(answers, "answers", true, AMOUNT);
        insert(comments, "comments", true, AMOUNT);
        insert(votes, "votes", true, votes.size());
    }

    private static void insert(List<?> objects, String createdObjectName, boolean debug, int amount){
        timer.start();

        try {
            POST.post(objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        long timeToCreate = timer.getElapsedTime();

        if(debug) {
            System.out.println("Created " + amount + " " +  createdObjectName + " in " + timeToCreate + "ms.");
        }
    }
}