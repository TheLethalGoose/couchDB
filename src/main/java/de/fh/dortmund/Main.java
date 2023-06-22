package de.fh.dortmund;

import de.fh.dortmund.couchDB.CouchInitializer;
import de.fh.dortmund.couchDB.CouchPersistor;
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

import java.util.List;

public class Main {
    private static final int AMOUNT_QUESTIONS = 1500;
    private static final int AMOUNT_ANSWERS = 2000;
    private static final int AMOUNT_TAGS = 20;
    private static final int AMOUNT_COMMENTS = 3000;
    private static final int AMOUNT_USERS = 5;
    private static final int TAG_AMOUNT_PER_QUESTION = 5;
    public static final String DATABASE_NAME = "stackoverflow";
    private static POST POST = new POST(DATABASE_NAME);
    static Timer timer = new Timer();

    private static final CouchPersistor persistor = new CouchPersistor();

    @SneakyThrows
    public static void main(String[] args) {

        CouchInitializer.init("localhost", 5984, DATABASE_NAME, "admin", "admin", true);

        // Generate test data
        List<User> users = UserGenerator.generateUsers(AMOUNT_USERS);
        List<Tag> tags = TagGenerator.generateTags(AMOUNT_TAGS);
        List<Question> questions = QuestionGenerator.generateQuestions(users, AMOUNT_QUESTIONS, TAG_AMOUNT_PER_QUESTION, tags);
        List<Answer> answers = AnswerGenerator.generateAnswers(questions, users, AMOUNT_ANSWERS);
        List<Comment> comments = CommentGenerator.generateComments(questions, users, AMOUNT_COMMENTS);
        List<Vote> votes = VoteGenerator.generateVotes(comments, questions, answers, users);

        // Insert data into database
        persistor.persist(users, "users", true, AMOUNT_USERS);
        persistor.persist(tags, "tags", true, AMOUNT_TAGS);
        persistor.persist(questions, "questions", true, AMOUNT_QUESTIONS);
        persistor.persist(answers, "answers", true, AMOUNT_ANSWERS);
        persistor.persist(comments, "comments", true, AMOUNT_COMMENTS);
        persistor.persist(votes, "votes", true, votes.size());
    }
}