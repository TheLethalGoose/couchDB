package de.fh.dortmund;

import de.fh.dortmund.couchDB.CouchInitializer;
import de.fh.dortmund.couchDB.CouchPersistor;
import de.fh.dortmund.fakedata.generator.post.AnswerGenerator;
import de.fh.dortmund.fakedata.generator.post.CommentGenerator;
import de.fh.dortmund.fakedata.generator.post.QuestionGenerator;
import de.fh.dortmund.fakedata.generator.tag.TagGenerator;
import de.fh.dortmund.fakedata.generator.user.UserGenerator;
import de.fh.dortmund.fakedata.generator.vote.VoteGenerator;
import de.fh.dortmund.models.*;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Objects;

public class Main {

    private static final String COUCHDB_HOST = "localhost";
    private static final int COUCHDB_PORT = 5984;

    private static final String COUCHDB_DATABASE = "stackoverflow";

    public static String COUCHDB_URL = "http://" + COUCHDB_HOST + ":" + COUCHDB_PORT + "/" + COUCHDB_DATABASE;


    private static final int AMOUNT_QUESTIONS = 1500;
    private static final int AMOUNT_ANSWERS = 2000;
    private static final int AMOUNT_TAGS = 20;
    private static final int AMOUNT_COMMENTS = 3000;
    private static final int AMOUNT_USERS = 1000;
    private static final int TAG_AMOUNT_PER_QUESTION = 5;
    public static final String DATABASE_NAME = "stackoverflow";

    @SneakyThrows
    public static void main(String[] args) {


        CouchInitializer.init(COUCHDB_HOST, COUCHDB_PORT, DATABASE_NAME, "admin", "admin", true);

        // Generate test data
        List<User> users = UserGenerator.generateUsers(AMOUNT_USERS);
        List<Tag> tags = TagGenerator.generateTags(AMOUNT_TAGS);
        List<Question> questions = QuestionGenerator.generateQuestions(users, AMOUNT_QUESTIONS, TAG_AMOUNT_PER_QUESTION, tags);
        List<Answer> answers = AnswerGenerator.generateAnswers(questions, users, AMOUNT_ANSWERS);
        List<Comment> comments = CommentGenerator.generateComments(questions, users, AMOUNT_COMMENTS);
        List<Vote> votes = VoteGenerator.generateVotes(comments, questions, answers, users);

        // Insert data into database
        CouchPersistor.persist(users, "users", true, AMOUNT_USERS);
        CouchPersistor.persist(tags, "tags", true, AMOUNT_TAGS);
        CouchPersistor.persist(questions, "questions", true, AMOUNT_QUESTIONS);
        CouchPersistor.persist(answers, "answers", true, AMOUNT_ANSWERS);
        CouchPersistor.persist(comments, "comments", true, AMOUNT_COMMENTS);
        CouchPersistor.persist(votes, "votes", true, Objects.requireNonNull(votes).size());


        //System.out.println(getDocsFromView("_design/tags","allTags",true, true)); //REDUCE FUNCTION  //ERWEITERUNG
        //System.out.println(getDocsFromView("_design/tags","popularTags",true, true)); //REDUCE FUNCTION //ERWEITERUNG

        //System.out.println(getDocsFromView("_design/users","allUsers"));
        //System.out.println(getDocsFromView("_design/users","byEmail","alexis.strosin@yahoo.com"));

        //System.out.println(getDocsFromView("_design/votes","allVotes"));
        //System.out.println(getDocsFromView("_design/votes","totalVotesToQuestion", "0dbb4013-a135-4dfe-85c1-30009d071e2e",true)); //REDUCE FUNCTION
        //System.out.println(getDocsFromView("_design/votes","votesToPost", "0dbb4013-a135-4dfe-85c1-30009d071e2e"));

        //System.out.println(getDocsFromView("_design/posts","allQuestions"));
        //System.out.println(getDocsFromView("_design/posts","allAnswers"));
        //System.out.println(getDocsFromView("_design/posts","allComments"));
        //System.out.println(getDocsFromView("_design/posts","allPosts"));
        //System.out.println(getDocsFromView("_design/posts","answersToQuestion", "0dbb4013-a135-4dfe-85c1-30009d071e2e"));
        //System.out.println(getDocsFromView("_design/posts","byUserId", "008c203b-c737-424d-82cf-9b56992ea743"));
        //System.out.println(getDocsFromView("_design/posts","byTagName", "application")); //ERWEITERUNG
        //System.out.println(getDocsFromView("_design/posts","latestQuestions"));

        //addPostToFavorites("00945701-66eb-4d1f-b84f-b347f5038cef", "0dbb4013-a135-4dfe-85c1-30009d071e2e"); //ERWEITERUNG
        //System.out.println(getDocsFromView("_design/users","userFavorites", "00945701-66eb-4d1f-b84f-b347f5038cef")); //ERWEITERUNG
        //updatePost("0dbb4013-a135-4dfe-85c1-30009d071e2e", "FML");
        //markAnswerAsAccepted("002e7a4b-ee51-4d2b-9577-faee515eabeb");

    }
}