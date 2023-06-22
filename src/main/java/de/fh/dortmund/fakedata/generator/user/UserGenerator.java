package de.fh.dortmund.fakedata.generator.user;

import com.github.javafaker.Faker;
import de.fh.dortmund.helper.Timer;
import de.fh.dortmund.models.User;
import de.fh.dortmund.service.POST;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserGenerator{

	static Faker faker = new Faker();

	public static List<User> generateUsers(int amount) {
		List<User> users = new ArrayList<>();

		for (int i = 0; i < amount; i++) {
			String username = faker.name().username();
			String password = faker.internet().password();
			String email = faker.internet().emailAddress();
			int reputation = faker.number().numberBetween(0, 10000);

			User newUser = new User(email,password,username, reputation);
			users.add(newUser);
		}

		return users;
	}
}
