package de.fh.dortmund.fakedata.generator.tag;

import com.github.javafaker.Faker;
import de.fh.dortmund.helper.Timer;
import de.fh.dortmund.models.Tag;
import de.fh.dortmund.models.User;
import de.fh.dortmund.service.POST;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TagGenerator{

    static Faker faker = new Faker();

    public static List<Tag> generateTags(int amount) {
        List<Tag> tags = new ArrayList<>();

        for(int tagCount = 0; tagCount < amount; tagCount++) {
            String tagName = faker.hacker().noun();
            String tagInfo = faker.lorem().sentence();
            Tag newTag = new Tag(tagName, tagInfo);

            tags.add(newTag);
        }

        return tags;
    }
}
