package tests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import iob.restAPI.InstanceBoundary;
import iob.restAPI.UserBoundary;

public class InstancesTests extends Base {

	@Test
	@DisplayName("TEST: POST Instance - with a valid user, returns the Instance boundry with id and timestemps")
	public void testPostInstanceWithValidUser() throws Exception {

		UserBoundary userBoundaryManager = this.restTemplate.postForObject(this.baseUrl + "/iob/users",
				Helper.getNewUserBoundary("MANAGER"), UserBoundary.class);

		InstanceBoundary rv = this.restTemplate.postForObject(
				this.baseUrl + "/iob/instances", Helper.getInstanceBoundary("testType", "testName",
						userBoundaryManager.getUserId().getDomain(), userBoundaryManager.getUserId().getEmail()),
				InstanceBoundary.class);

		assertThat(rv).isNotNull();
	}

	@Test
	@DisplayName("TEST: POST new instance - with a non valid user, throws BAD_REQUEST exception")
	public void testPostInstanceWithNonValidUser() throws Exception {

		InstanceBoundary rv = null;
		try {
			rv = this.restTemplate.postForObject(this.baseUrl + "/iob/instances",
					Helper.getInstanceBoundary("testType", "testName", "domain", "email@gmail.com"),
					InstanceBoundary.class);
		} catch (BadRequest e) {
			System.err.println(e);
			assertThat(true);
		}

		assertThat(false);
	}

	@Test
	@DisplayName("TEST: GET specific instance - by instance domain and id, with a valid user that has a manager role, returns the Instance boundry")
	public void testGetSpecificInstanceWithValidUserManager() throws Exception {

		UserBoundary userBoundaryManager = this.restTemplate.postForObject(this.baseUrl + "/iob/users",
				Helper.getNewUserBoundary("MANAGER"), UserBoundary.class);

		InstanceBoundary instanceBoundary = this.restTemplate.postForObject(
				this.baseUrl + "/iob/instances", Helper.getInstanceBoundary("testType", "testName",
						userBoundaryManager.getUserId().getDomain(), userBoundaryManager.getUserId().getEmail()),
				InstanceBoundary.class);

		InstanceBoundary actual = this.restTemplate.getForObject(
				this.baseUrl
						+ "/iob/instances/{instanceDomain}/{instanceId}?userDomain={userDomain}&userEmail={userEmail}",
				InstanceBoundary.class, instanceBoundary.getInstanceId().getDomain(),
				instanceBoundary.getInstanceId().getId(), userBoundaryManager.getUserId().getDomain(),
				userBoundaryManager.getUserId().getEmail());

		assertThat(actual).isNotNull();
	}

	@Test
	@DisplayName("TEST: PUT update instance - by instance domain and id wih a new instamce boundary, with a valid user that has a manager role")
	public void testPutUpdateInstanceWithValidUserManager() throws Exception {

		UserBoundary userBoundaryManager = this.restTemplate.postForObject(this.baseUrl + "/iob/users",
				Helper.getNewUserBoundary("MANAGER"), UserBoundary.class);

		InstanceBoundary instanceBoundary = this.restTemplate.postForObject(
				this.baseUrl + "/iob/instances", Helper.getInstanceBoundary("testType", "testName",
						userBoundaryManager.getUserId().getDomain(), userBoundaryManager.getUserId().getEmail()),
				InstanceBoundary.class);

		double latOld = instanceBoundary.getLocation().getLat();

		Helper.updateInstanceBoundary(instanceBoundary);

		this.restTemplate.put(
				this.baseUrl
						+ "/iob/instances/{instanceDomain}/{instanceId}?userDomain={userDomain}&userEmail={userEmail}",
				instanceBoundary, instanceBoundary.getInstanceId().getDomain(),
				instanceBoundary.getInstanceId().getId(), userBoundaryManager.getUserId().getDomain(),
				userBoundaryManager.getUserId().getEmail());

		InstanceBoundary actual = this.restTemplate.getForObject(
				this.baseUrl
						+ "/iob/instances/{instanceDomain}/{instanceId}?userDomain={userDomain}&userEmail={userEmail}",
				InstanceBoundary.class, instanceBoundary.getInstanceId().getDomain(),
				instanceBoundary.getInstanceId().getId(), userBoundaryManager.getUserId().getDomain(),
				userBoundaryManager.getUserId().getEmail());

		assertThat(latOld != actual.getLocation().getLat()
				&& actual.getLocation().getLat() == instanceBoundary.getLocation().getLat());
	}

	@Test
	@DisplayName("TEST: GET all Instances - with a valid user that has manager role")
	public void testGetAllInstanceWithValidUserManager() throws Exception {

		UserBoundary userBoundaryManager = this.restTemplate.postForObject(this.baseUrl + "/iob/users",
				Helper.getNewUserBoundary("MANAGER"), UserBoundary.class);

		for (int i = 0; i < 10; i++) {
			this.restTemplate.postForObject(
					this.baseUrl + "/iob/instances", Helper.getInstanceBoundary("testType", "testName",
							userBoundaryManager.getUserId().getDomain(), userBoundaryManager.getUserId().getEmail()),
					InstanceBoundary.class);
		}

		InstanceBoundary[] actual = this.restTemplate.getForObject(
				baseUrl + "/iob/instances?userDomain={userDomain}&userEmail={userEmail}&size={size}&page={page}",
				InstanceBoundary[].class, userBoundaryManager.getUserId().getDomain(),
				userBoundaryManager.getUserId().getEmail(), 10, 0);
		
		assertThat(actual).isNotNull().hasSize(10);
	}

	@Test
	@DisplayName("TEST: GET all Instances - with a valid user that has player role, acive instances only")
	public void testGetAllInstanceWithValidUserPlayer() throws Exception {

		UserBoundary userBoundaryPlayer = this.restTemplate.postForObject(this.baseUrl + "/iob/users",
				Helper.getNewUserBoundary("PLAYER"), UserBoundary.class);

		UserBoundary userBoundaryManager = this.restTemplate.postForObject(this.baseUrl + "/iob/users",
				Helper.getNewUserBoundary("MANAGER"), UserBoundary.class);
		int count = 0;

		for (int i = 0; i < 10; i++) {
			InstanceBoundary instanceBoundary = Helper.getInstanceBoundary("testType", "testName",
					userBoundaryManager.getUserId().getDomain(), userBoundaryManager.getUserId().getEmail());
			if (instanceBoundary.getActive())
				count++;
			this.restTemplate.postForObject(this.baseUrl + "/iob/instances", instanceBoundary, InstanceBoundary.class);
		}

		InstanceBoundary[] actual = this.restTemplate.getForObject(
				baseUrl + "/iob/instances?userDomain={userDomain}&userEmail={userEmail}&size={size}&page={page}",
				InstanceBoundary[].class, userBoundaryPlayer.getUserId().getDomain(),
				userBoundaryPlayer.getUserId().getEmail(), 10, 0);
		System.err.println(actual);
		assertThat(actual).isNotNull().hasSize(count);
	}

	@Test
	@DisplayName("TEST: DELETE all Instances - with a valid user that has admin role")
	public void testDeleteAllInstanceWithValidUserAdmin() throws Exception {

		UserBoundary userBoundaryAdmin = this.restTemplate.postForObject(this.baseUrl + "/iob/users",
				Helper.getNewUserBoundary("ADMIN"), UserBoundary.class);

		UserBoundary userBoundaryManager = this.restTemplate.postForObject(this.baseUrl + "/iob/users",
				Helper.getNewUserBoundary("MANAGER"), UserBoundary.class);

		for (int i = 0; i < 10; i++) {
			this.restTemplate.postForObject(
					this.baseUrl + "/iob/instances", Helper.getInstanceBoundary("testType", "testName",
							userBoundaryManager.getUserId().getDomain(), userBoundaryManager.getUserId().getEmail()),
					InstanceBoundary.class);
		}

		this.restTemplate.delete(this.baseUrl + "/iob/admin/instances?userDomain={userDomain}&userEmail={userEmail}",
				userBoundaryAdmin.getUserId().getDomain(), userBoundaryAdmin.getUserId().getEmail());
		
		InstanceBoundary[] actual = this.restTemplate.getForObject(
				baseUrl + "/iob/instances?userDomain={userDomain}&userEmail={userEmail}&size={size}&page={page}",
				InstanceBoundary[].class, userBoundaryManager.getUserId().getDomain(),
				userBoundaryManager.getUserId().getEmail(), 10, 0);
		
		assertThat(actual).isNotNull().hasSize(0);
	}
	
	@Test
	@DisplayName("TEST: GET all Instances by name - with a valid user that has player role, acive instances only")
	public void testGetAllInstanceByNameWithValidUserPlayer() throws Exception {

		UserBoundary userBoundaryPlayer = this.restTemplate.postForObject(this.baseUrl + "/iob/users",
				Helper.getNewUserBoundary("PLAYER"), UserBoundary.class);
		
		UserBoundary userBoundaryManager = this.restTemplate.postForObject(this.baseUrl + "/iob/users",
				Helper.getNewUserBoundary("MANAGER"), UserBoundary.class);

		int count = 0;

		for (int i = 0; i < 10; i++) {
			InstanceBoundary instanceBoundary = Helper.getInstanceBoundary("testType", "name",
					userBoundaryManager.getUserId().getDomain(), userBoundaryManager.getUserId().getEmail());
			if (instanceBoundary.getActive())
				count++;
			InstanceBoundary instanceBoundary2 = Helper.getInstanceBoundary("testType", "abcd",
					userBoundaryManager.getUserId().getDomain(), userBoundaryManager.getUserId().getEmail());
			this.restTemplate.postForObject(this.baseUrl + "/iob/instances", instanceBoundary, InstanceBoundary.class);
			this.restTemplate.postForObject(this.baseUrl + "/iob/instances", instanceBoundary2, InstanceBoundary.class);
		}

		InstanceBoundary[] actual = this.restTemplate.getForObject(
				baseUrl + "/iob/instances/search/byName/{name}?userDomain={userDomain}&userEmail={userEmail}&size={size}&page={page}",
				InstanceBoundary[].class, "name", userBoundaryPlayer.getUserId().getDomain(),
				userBoundaryPlayer.getUserId().getEmail(), 20, 0);
		System.err.println(actual);
		assertThat(actual).isNotNull().hasSize(count);
	}
	
	@Test
	@DisplayName("TEST: GET all Instances by name - with a valid user that has manager role")
	public void testGetAllInstanceByNameWithValidUserManager() throws Exception {
		
		UserBoundary userBoundaryManager = this.restTemplate.postForObject(this.baseUrl + "/iob/users",
				Helper.getNewUserBoundary("MANAGER"), UserBoundary.class);

		int count = 0;

		for (int i = 0; i < 10; i++) {
			InstanceBoundary instanceBoundary = Helper.getInstanceBoundary("testType", "name",
					userBoundaryManager.getUserId().getDomain(), userBoundaryManager.getUserId().getEmail());
			count++;
			InstanceBoundary instanceBoundary2 = Helper.getInstanceBoundary("testType", "abcd",
					userBoundaryManager.getUserId().getDomain(), userBoundaryManager.getUserId().getEmail());
			this.restTemplate.postForObject(this.baseUrl + "/iob/instances", instanceBoundary, InstanceBoundary.class);
			this.restTemplate.postForObject(this.baseUrl + "/iob/instances", instanceBoundary2, InstanceBoundary.class);
		}

		InstanceBoundary[] actual = this.restTemplate.getForObject(
				baseUrl + "/iob/instances/search/byName/{name}?userDomain={userDomain}&userEmail={userEmail}&size={size}&page={page}",
				InstanceBoundary[].class, "name", userBoundaryManager.getUserId().getDomain(),
				userBoundaryManager.getUserId().getEmail(), 20, 0);
		System.err.println(actual);
		assertThat(actual).isNotNull().hasSize(count);
	}
	
	@Test
	@DisplayName("TEST: GET all Instances by type - with a valid user that has player role, acive instances only")
	public void testGetAllInstanceByTypeWithValidUserPlayer() throws Exception {

		UserBoundary userBoundaryPlayer = this.restTemplate.postForObject(this.baseUrl + "/iob/users",
				Helper.getNewUserBoundary("PLAYER"), UserBoundary.class);
		
		UserBoundary userBoundaryManager = this.restTemplate.postForObject(this.baseUrl + "/iob/users",
				Helper.getNewUserBoundary("MANAGER"), UserBoundary.class);

		int count = 0;

		for (int i = 0; i < 10; i++) {
			InstanceBoundary instanceBoundary = Helper.getInstanceBoundary("type1", "name",
					userBoundaryManager.getUserId().getDomain(), userBoundaryManager.getUserId().getEmail());
			if (instanceBoundary.getActive())
				count++;
			InstanceBoundary instanceBoundary2 = Helper.getInstanceBoundary("type2", "name",
					userBoundaryManager.getUserId().getDomain(), userBoundaryManager.getUserId().getEmail());
			this.restTemplate.postForObject(this.baseUrl + "/iob/instances", instanceBoundary, InstanceBoundary.class);
			this.restTemplate.postForObject(this.baseUrl + "/iob/instances", instanceBoundary2, InstanceBoundary.class);
		}

		InstanceBoundary[] actual = this.restTemplate.getForObject(
				baseUrl + "/iob/instances/search/byType/{type}?userDomain={userDomain}&userEmail={userEmail}&size={size}&page={page}",
				InstanceBoundary[].class, "type1", userBoundaryPlayer.getUserId().getDomain(),
				userBoundaryPlayer.getUserId().getEmail(), 20, 0);
		System.err.println(actual);
		assertThat(actual).isNotNull().hasSize(count);
	}
	
	@Test
	@DisplayName("TEST: GET all Instances by type - with a valid user that has manager role")
	public void testGetAllInstanceByTypeWithValidUserManager() throws Exception {
		
		UserBoundary userBoundaryManager = this.restTemplate.postForObject(this.baseUrl + "/iob/users",
				Helper.getNewUserBoundary("MANAGER"), UserBoundary.class);

		int count = 0;

		for (int i = 0; i < 10; i++) {
			InstanceBoundary instanceBoundary = Helper.getInstanceBoundary("type1", "name",
					userBoundaryManager.getUserId().getDomain(), userBoundaryManager.getUserId().getEmail());
			count++;
			InstanceBoundary instanceBoundary2 = Helper.getInstanceBoundary("type2", "name",
					userBoundaryManager.getUserId().getDomain(), userBoundaryManager.getUserId().getEmail());
			this.restTemplate.postForObject(this.baseUrl + "/iob/instances", instanceBoundary, InstanceBoundary.class);
			this.restTemplate.postForObject(this.baseUrl + "/iob/instances", instanceBoundary2, InstanceBoundary.class);
		}

		InstanceBoundary[] actual = this.restTemplate.getForObject(
				baseUrl + "/iob/instances/search/byType/{type}?userDomain={userDomain}&userEmail={userEmail}&size={size}&page={page}",
				InstanceBoundary[].class, "type1", userBoundaryManager.getUserId().getDomain(),
				userBoundaryManager.getUserId().getEmail(), 20, 0);
		System.err.println(actual);
		assertThat(actual).isNotNull().hasSize(count);
	}
	
	@Test
	@DisplayName("TEST: GET all Instances by location - with a valid user that has manager role")
	public void testGetAllInstanceByLocationWithValidUserManager() throws Exception {
		
		UserBoundary userBoundaryManager = this.restTemplate.postForObject(this.baseUrl + "/iob/users",
				Helper.getNewUserBoundary("MANAGER"), UserBoundary.class);

		int count = 0;

		for (int i = 0; i < 100; i++) {
			InstanceBoundary instanceBoundary = Helper.getInstanceBoundary("type1", "name",
					userBoundaryManager.getUserId().getDomain(), userBoundaryManager.getUserId().getEmail());
			
			count++;
			this.restTemplate.postForObject(this.baseUrl + "/iob/instances", instanceBoundary, InstanceBoundary.class);
		}

		InstanceBoundary[] actual = this.restTemplate.getForObject(
				baseUrl + "/iob/instances/search/near/{lat}/{lng}/{distance}?userDomain={userDomain}&userEmail={userEmail}&size={size}&page={page}",
				InstanceBoundary[].class, 15, 15, 5, userBoundaryManager.getUserId().getDomain(),
				userBoundaryManager.getUserId().getEmail(), 100, 0);
		System.err.println(actual);
		assertThat(actual).isNotNull().hasSize(count);
	}
}
