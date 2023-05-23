package de.fh.dortmund;

import de.fh.dortmund.couchDB.couchInitializer;

public class Main {
    public static void main(String[] args) {

        couchInitializer.init("localhost", 5984, "stackoverflow", "admin", "admin");

    }
}