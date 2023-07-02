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
import de.fh.dortmund.util.PerformanceMonitor;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Objects;

import static de.fh.dortmund.service.GET.getDocsFromView;
import static de.fh.dortmund.service.PUT.*;

public class Main {

    private static final String COUCHDB_HOST = "localhost";
    private static final int COUCHDB_PORT = 5984;

    private static final String COUCHDB_DATABASE = "stackoverflow";

    public static String COUCHDB_URL = "http://" + COUCHDB_HOST + ":" + COUCHDB_PORT + "/" + COUCHDB_DATABASE;

    public static final String DATABASE_NAME = "stackoverflow";

    @SneakyThrows
    public static void main(String[] args) {
        CouchInitializer.init(COUCHDB_HOST, COUCHDB_PORT, DATABASE_NAME, "admin", "admin", true);

        PerformanceMonitor monitor = new PerformanceMonitor(2, 10);
        monitor.runPerformanceTest();

    }
}