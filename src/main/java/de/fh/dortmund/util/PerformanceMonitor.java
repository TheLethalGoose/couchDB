package de.fh.dortmund.util;

import de.fh.dortmund.couchDB.CouchPersistor;
import de.fh.dortmund.fakedata.destroyer.post.PostDestroyer;
import de.fh.dortmund.fakedata.editor.PostEditor;
import de.fh.dortmund.fakedata.generator.post.AnswerGenerator;
import de.fh.dortmund.fakedata.generator.post.CommentGenerator;
import de.fh.dortmund.fakedata.generator.post.QuestionGenerator;
import de.fh.dortmund.fakedata.generator.tag.TagGenerator;
import de.fh.dortmund.fakedata.generator.user.UserGenerator;
import de.fh.dortmund.fakedata.generator.vote.VoteGenerator;
import de.fh.dortmund.fakedata.receiver.PostReceiver;
import de.fh.dortmund.fakedata.receiver.TagReceiver;
import de.fh.dortmund.fakedata.receiver.UserReceiver;
import de.fh.dortmund.fakedata.receiver.VoteReceiver;
import de.fh.dortmund.helper.Timer;
import de.fh.dortmund.models.*;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

import static de.fh.dortmund.helper.Statistics.*;
import static de.fh.dortmund.helper.Timer.convertMilliSeconds;

public class PerformanceMonitor {
    int fetchIterations;
    int ddlIterations;

    List<User> users = new ArrayList<>();
    List<Question> questions = new ArrayList<>();
    List<Answer> answers = new ArrayList<>();
    List<Tag> tags = new ArrayList<>();

    List<Comment> comments = new ArrayList<>();

    List<Vote> votes = new ArrayList<>();

    long[] generatedUsersInTimes;
    long[] generatedQuestionsInTimes;
    long[] generatedAnswersInTimes;
    long[] generatedCommentsInTimes;
    long[] generatedVotesInTimes;
    long[] destroyedUsersInTimes;
    long[] destroyedQuestionsAndAnswersAndCommentsInTimes;
    long[] destroyedAnswersInTimes;
    long[] destroyedCommentsInTimes;
    long[] timesToGenerate;
    long[] timesToDestroy;

    public PerformanceMonitor(int ddlIterations, int fetchIterations) {
        this.ddlIterations = ddlIterations;
        this.fetchIterations = fetchIterations;

        generatedUsersInTimes = new long[ddlIterations];
        generatedQuestionsInTimes = new long[ddlIterations];
        generatedAnswersInTimes = new long[ddlIterations];
        generatedCommentsInTimes = new long[ddlIterations];
        generatedVotesInTimes = new long[ddlIterations];
        destroyedUsersInTimes = new long[ddlIterations];
        destroyedQuestionsAndAnswersAndCommentsInTimes = new long[ddlIterations];
        destroyedAnswersInTimes = new long[ddlIterations];
        destroyedCommentsInTimes = new long[ddlIterations];
        timesToGenerate = new long[ddlIterations];
        timesToDestroy = new long[ddlIterations];
    }
    @SneakyThrows
    public void runPerformanceTest(){
        createAndDestroyDataTest(2500, 1500, 2000, 3000, 5, 1500, 2000, 3000,5, 2);

        //Fetch and edit data test
        generateTestData(2500, 3000, 2000, 1500, 2);
        fetchDataTest();
        editDataTest(1000);
    }

    @SneakyThrows
    private void createAndDestroyDataTest(int usersToGenerate, int questionsToGenerate, int answersToGenerate, int commentsToGenerate, int tagsToGenerate, int questionsToDestroy, int answersToDestroy, int commentsToDestroy, int tagsPerQuestion, int votesToGeneratePerPost){

        Timer timer = new Timer();

        for(int i = 0; i < ddlIterations; i++){

            System.out.println("Create/Remove Iteration " + (i+1) + " of " + ddlIterations);

            // Generate local test data
            users = UserGenerator.generateUsers(usersToGenerate);
            tags = TagGenerator.generateTags(tagsToGenerate);
            questions = QuestionGenerator.generateQuestions(users, questionsToGenerate, tagsPerQuestion, tags);
            comments = CommentGenerator.generateComments(questions, users, commentsToGenerate);
            answers = AnswerGenerator.generateAnswers(questions, users, answersToGenerate);
            votes = VoteGenerator.generateVotes(comments, questions, answers, users, votesToGeneratePerPost);

            // Test performance of generation and deletion of users, questions and answers
            timer.start();

            // Persist in database and retrieve times
            long generatedUsersIn = CouchPersistor.persist(users, false);
            long generatedQuestionsIn = CouchPersistor.persist(questions, false);
            long generatedAnswersIn = CouchPersistor.persist(answers, false);
            long generatedCommentsIn = CouchPersistor.persist(comments, false);
            long generatedVotesIn = CouchPersistor.persist(votes, false);

            long generatedIn = timer.getElapsedTime();

            generatedUsersInTimes[i] = generatedUsersIn;
            generatedQuestionsInTimes[i] = generatedQuestionsIn;
            generatedAnswersInTimes[i] = generatedAnswersIn;
            generatedCommentsInTimes[i] = generatedCommentsIn;
            generatedVotesInTimes[i] = generatedVotesIn;
            timesToGenerate[i] = generatedIn;

            // Deletion from database
            //long destroyedUsersIn = UserDestroyer.destroyUsers(users, usersToDestroy, false);
            long destroyedQuestionsAndAnswersAndCommentsIn = PostDestroyer.destroyQuestions(questions, answers, comments, votes, questionsToDestroy,false);
            //long destroyedAnswersIn = PostDestroyer.destroyAnswers(answers, votes, answersToDestroy, false);
            //long destroyedCommentsIn = PostDestroyer.destroyComments(comments, votes, commentsToDestroy, false);

            long destroyedIn = timer.getElapsedTime();

            //destroyedUsersInTimes[i] = destroyedUsersIn;
            destroyedQuestionsAndAnswersAndCommentsInTimes[i] = destroyedQuestionsAndAnswersAndCommentsIn;
            //destroyedAnswersInTimes[i] = destroyedAnswersIn;
            //destroyedCommentsInTimes[i] = destroyedCommentsIn;
            timesToDestroy[i] = destroyedIn;


            // Console output for current run
            if(ddlIterations == 1){
                System.out.println("Finsished generating test data in " + convertMilliSeconds(generatedIn));
                System.out.println("Generated " + usersToGenerate + " users in " + convertMilliSeconds(generatedUsersIn));
                System.out.println("Generated " + questionsToGenerate + " questions in " + convertMilliSeconds(generatedQuestionsIn));
                System.out.println("Generated " + answersToGenerate + " answers in " + convertMilliSeconds(generatedAnswersIn));
                System.out.println("Generated " + (questionsToGenerate + answersToGenerate + commentsToGenerate) * votesToGeneratePerPost + " votes in " + convertMilliSeconds(generatedVotesIn));
                System.out.println("-----------------------------------------");
                System.out.println("Finsished destroying test data in " + convertMilliSeconds(timer.getElapsedTime()));
                //System.out.println("Destroyed " + usersToDestroy + " users in " + convertMilliSeconds(destroyedUsersIn));
                System.out.println("Destroyed " + questionsToDestroy + " questions in " + convertMilliSeconds(destroyedQuestionsAndAnswersAndCommentsIn));
                //System.out.println("Destroyed " + answersToDestroy + " answers in " + convertMilliSeconds(destroyedAnswersIn));
            }

        }

        // Console output for all runs
        if(ddlIterations > 1){
            System.out.println("Average time of generating users: " + convertMilliSeconds(calculateAverage(generatedUsersInTimes)));
            System.out.println("Median time of generating users: " + convertMilliSeconds(calculateMedian(generatedUsersInTimes)));
            System.out.println("Min time of generating users: " + convertMilliSeconds(min(generatedUsersInTimes)));
            System.out.println("Max time of generating users: " + convertMilliSeconds(max(generatedUsersInTimes)));
            System.out.println("-----------------------------------------");

            System.out.println("Average time of generating questions: " + convertMilliSeconds(calculateAverage(generatedQuestionsInTimes)));
            System.out.println("Median time of generating questions: " + convertMilliSeconds(calculateMedian(generatedQuestionsInTimes)));
            System.out.println("Min time of generating questions: " + convertMilliSeconds(min(generatedQuestionsInTimes)));
            System.out.println("Max time of generating questions: " + convertMilliSeconds(max(generatedQuestionsInTimes)));
            System.out.println("-----------------------------------------");

            System.out.println("Average time of generating answers: " + convertMilliSeconds(calculateAverage(generatedAnswersInTimes)));
            System.out.println("Median time of generating answers: " + convertMilliSeconds(calculateMedian(generatedAnswersInTimes)));
            System.out.println("Min time of generating answers: " + convertMilliSeconds(min(generatedAnswersInTimes)));
            System.out.println("Max time of generating answers: " + convertMilliSeconds(max(generatedAnswersInTimes)));
            System.out.println("-----------------------------------------");

            System.out.println("Average time of generating comments: " + convertMilliSeconds(calculateAverage(generatedCommentsInTimes)));
            System.out.println("Median time of generating comments: " + convertMilliSeconds(calculateMedian(generatedCommentsInTimes)));
            System.out.println("Min time of generating comments: " + convertMilliSeconds(min(generatedCommentsInTimes)));
            System.out.println("Max time of generating comments: " + convertMilliSeconds(max(generatedCommentsInTimes)));
            System.out.println("-----------------------------------------");

            System.out.println("Average time of generating votes: " + convertMilliSeconds(calculateAverage(generatedVotesInTimes)));
            System.out.println("Median time of generating votes: " + convertMilliSeconds(calculateMedian(generatedVotesInTimes)));
            System.out.println("Min time of generating votes: " + convertMilliSeconds(min(generatedVotesInTimes)));
            System.out.println("Max time of generating votes: " + convertMilliSeconds(max(generatedVotesInTimes)));
            System.out.println("-----------------------------------------");

            System.out.println("-----------------------------------------");
            System.out.println("Average time of generating test data: " + convertMilliSeconds(calculateAverage(timesToGenerate)));
            System.out.println("Median time of generating test data: " + convertMilliSeconds(calculateMedian(timesToGenerate)));
            System.out.println("Min time of generating test data: " + convertMilliSeconds(min(timesToGenerate)));
            System.out.println("Max time of destroying test data: " + convertMilliSeconds(max(timesToGenerate)));
            System.out.println("-----------------------------------------");

            System.out.println("Average time of destroying test data: " + convertMilliSeconds(calculateAverage(timesToDestroy)));
            System.out.println("Median time of destroying test data: " + convertMilliSeconds(calculateMedian(timesToDestroy)));
            System.out.println("Min time of destroying test data: " + convertMilliSeconds(min(timesToDestroy)));
            System.out.println("Max time of destroying test data: " + convertMilliSeconds(max(timesToDestroy)));

        }

        System.out.println("------------------------------------------------------------------");

    }
    private void fetchDataTest(){
        System.out.println("Starting fetch data test");

        System.out.println("Median time to fetch all users " + fetchIterations + " iterations: " + convertMilliSeconds(UserReceiver.medianTimeToFetchAllUsers(fetchIterations)));
        System.out.println("Median time to fetch users by their email of " + fetchIterations + " iterations: " + convertMilliSeconds(UserReceiver.medianTimeToFetchUsersByEmail(users, fetchIterations)));
        System.out.println("Median time to fetch all user favorites of " + fetchIterations + " iterations: " + convertMilliSeconds(UserReceiver.medianTimeToFetchUserFavorites(users, fetchIterations)));

        System.out.println("Median time to fetch all questions of " + fetchIterations + " iterations: " + convertMilliSeconds(PostReceiver.medianTimeToFetchAllQuestions(fetchIterations)));
        System.out.println("Median time to fetch all answers of " + fetchIterations + " iterations: " + convertMilliSeconds(PostReceiver.medianTimeToFetchAllAnswers(fetchIterations)));
        System.out.println("Median time to fetch all comments of " + fetchIterations + " iterations: " + convertMilliSeconds(PostReceiver.medianTimeToFetchAllComments(fetchIterations)));
        System.out.println("Median time to fetch all posts of " + fetchIterations + " iterations: " + convertMilliSeconds(PostReceiver.medianTimeToFetchAllPosts(fetchIterations)));
        System.out.println("Median time to fetch posts by userId " + fetchIterations + " iterations: " + convertMilliSeconds(PostReceiver.medianTimeToFetchQuestionsByUserId(users, fetchIterations)));
        System.out.println("(EXTENSION) Median time to fetch posts by tag name " + fetchIterations + " iterations: " + convertMilliSeconds(PostReceiver.medianTimeToFetchPostsByTagName(tags, fetchIterations)));
        System.out.println("Median time to fetch answers by their question of " + fetchIterations + " iterations: " + convertMilliSeconds(PostReceiver.medianTimeToFetchAnswersByQuestion(questions, fetchIterations)));
        System.out.println("Median time to fetch the latest question " + fetchIterations + " iterations: " + convertMilliSeconds(PostReceiver.medianTimeToFetchLatestQuestions(fetchIterations)));

        System.out.println("Median time to fetch all votes of " + fetchIterations + " iterations: " + convertMilliSeconds(VoteReceiver.medianTimeToFetchAllVotes( fetchIterations)));
        System.out.println("(REDUCE) Median time to fetch the net votes of a question of " + fetchIterations + " iterations: " + convertMilliSeconds(VoteReceiver.medianTimeToFetchTotalVotesToQuestion(questions, fetchIterations)));

        System.out.println("(REDUCE + EXTENSION) Median time to fetch all tags of " + fetchIterations + " iterations: " + convertMilliSeconds(TagReceiver.medianTimeToFetchAllTags(fetchIterations)));
        System.out.println("(REDUCE + EXTENSION) Median time to fetch popular tags of " + fetchIterations + " iterations: " + convertMilliSeconds(TagReceiver.medianTimeToFetchPopularTags(fetchIterations)));

        System.out.println("------------------------------------------------------------------");
    }

    private void editDataTest(int questionsToAlter){
        System.out.println("Starting edit data test");

        System.out.println("Median time to edit content of question of " + fetchIterations + " iterations: " + convertMilliSeconds(PostEditor.medianTimeToEditQuestion(questions, fetchIterations, questionsToAlter)));
        System.out.println("Median time to mark answer as accepted of " + fetchIterations + " iterations: " + convertMilliSeconds(PostEditor.medianTimeToAcceptAnswer(answers, fetchIterations)));
        System.out.println("(EXTENSION) Median time to moderate question of " + fetchIterations + " iterations: " + convertMilliSeconds(PostEditor.medianTimeToModerateQuestions(questions, fetchIterations)));
        System.out.println("(EXTENSION) Median time to add question to favorites of " + fetchIterations + " iterations: " + convertMilliSeconds(PostEditor.medianTimeToAddQuestionToFavorites(users, questions, fetchIterations)));

        System.out.println("------------------------------------------------------------------");
    }

    @SneakyThrows
    public void generateTestData(int usersToGenerate,int commentsToGenerate, int answersToGenerate, int questionsToGenerate, int votesToGeneratePerPost){
        System.out.println("Generating test data");

        // Generate additional local data
        users = UserGenerator.generateUsers(usersToGenerate);
        questions = QuestionGenerator.generateQuestions(users, questionsToGenerate, 5, tags);
        answers = AnswerGenerator.generateAnswers(questions, users, answersToGenerate);
        comments = CommentGenerator.generateComments(questions, users, commentsToGenerate);
        votes = VoteGenerator.generateVotes(comments, questions, answers, users, votesToGeneratePerPost);

        // Insert data into database
        CouchPersistor.persist(users, false);
        CouchPersistor.persist(questions, false);
        CouchPersistor.persist(answers, false);
        CouchPersistor.persist(comments, false);
        CouchPersistor.persist(votes, false);
    }
}
