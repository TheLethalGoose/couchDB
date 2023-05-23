package de.fh.dortmund;

import de.fh.dortmund.couchDB.couchInitializer;
import de.fh.dortmund.fakedata.user.UserGenerator;
import de.fh.dortmund.models.User;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

public class Main {

    @SneakyThrows
    public static void main(String[] args) {

        couchInitializer.init("localhost", 5984, "stackoverflow", "admin", "admin");

        List<User> users = new ArrayList<>();

        UserGenerator.generateUsers(users, 1000, true);

    }
}