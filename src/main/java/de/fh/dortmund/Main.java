package de.fh.dortmund;

import de.fh.dortmund.couchDB.CouchInitializer;
import de.fh.dortmund.util.PerformanceMonitor;
import lombok.SneakyThrows;

public class Main {

    private static final String COUCHDB_HOST = "localhost";
    private static final int COUCHDB_PORT = 5984;

    private static final String COUCHDB_DATABASE = "stackoverflow";

    public static String COUCHDB_URL = "http://" + COUCHDB_HOST + ":" + COUCHDB_PORT + "/" + COUCHDB_DATABASE;

    public static final String DATABASE_NAME = "stackoverflow";

    @SneakyThrows
    public static void main(String[] args) {
        CouchInitializer.init(COUCHDB_HOST, COUCHDB_PORT, DATABASE_NAME, "admin", "admin", true);
        PerformanceMonitor monitor = new PerformanceMonitor(2, 2);
        monitor.runPerformanceTest();
    }
}