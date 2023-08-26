package tests;

import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Value;

import iob.restAPI.InstanceBoundary;
import iob.restAPI.NewUserBoundary;
import iob.utility.CreatedBy;
import iob.utility.DomainWithEmail;
import iob.utility.DomainWithId;
import iob.utility.Location;

public abstract class Helper {

	public static NewUserBoundary getNewUserBoundary(String role) {
		NewUserBoundary newUserBoundary = new NewUserBoundary();
		newUserBoundary.setEmail(role + "User@gmail.com");
		newUserBoundary.setAvatar("avatar");
		newUserBoundary.setRole(role);
		newUserBoundary.setUsername("userName");
		return newUserBoundary;
	}
	
	public static NewUserBoundary getNewUserBoundaryWithNumber(String role, int num) {
		NewUserBoundary newUserBoundary = new NewUserBoundary();
		newUserBoundary.setEmail(role + "User" + num + "@gmail.com");
		newUserBoundary.setAvatar("avatar");
		newUserBoundary.setRole(role);
		newUserBoundary.setUsername("userName" + num);
		return newUserBoundary;
	}

	public static InstanceBoundary getInstanceBoundary(String type, String name, String domain, String email) {
		InstanceBoundary instanceBoundary = new InstanceBoundary();

		instanceBoundary.setType(type);
		instanceBoundary.setName(name);
		instanceBoundary.setCreatedBy(new CreatedBy(new DomainWithEmail(domain, email)));

		Random rd = new Random();

		instanceBoundary.setActive(rd.nextBoolean());
		instanceBoundary.setLocation(new Location(ThreadLocalRandom.current().nextDouble(10, 20),
				ThreadLocalRandom.current().nextDouble(10, 20)));

		HashMap<String, Object> map = new HashMap<>();
		map.put("key1", rd.nextBoolean());
		map.put("key2", rd.nextDouble());
		map.put("key3", rd.nextInt());
		instanceBoundary.setInstanceAttributes(map);

		return instanceBoundary;
	}

	public static void updateInstanceBoundary(InstanceBoundary old) {
		Random rd = new Random();
		old.setActive(rd.nextBoolean());
		old.setLocation(new Location(rd.nextDouble() * 10, rd.nextDouble() * 10));

		HashMap<String, Object> map = new HashMap<>();
		map.put("key1", rd.nextBoolean());
		map.put("key2", rd.nextDouble());
		map.put("key3", rd.nextInt());
		old.setInstanceAttributes(map);
	}
}
