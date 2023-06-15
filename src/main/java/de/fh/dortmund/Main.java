package de.fh.dortmund;

import de.fh.dortmund.couchDB.couchInitializer;
import de.fh.dortmund.fakedata.generator.post.QuestionGenerator;
import de.fh.dortmund.fakedata.generator.user.UserGenerator;
import de.fh.dortmund.models.Question;
import de.fh.dortmund.models.Tag;
import de.fh.dortmund.models.User;
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
        HashSet<Tag> tags = new HashSet<>();

        UserGenerator.generateUsers("stackoverflow", users, 1000, true);
        QuestionGenerator.generateQuestions("stackoverflow", questions, users, tags, 1000, 5, true);

    }
}