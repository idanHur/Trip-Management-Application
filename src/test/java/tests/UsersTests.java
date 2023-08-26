package tests;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import iob.restAPI.NewUserBoundary;
import iob.restAPI.UserBoundary;

public class UsersTests extends Base {

	@Test
	@DisplayName("TEST: add user to the database")
	public void testPostUserOnServer() throws Exception {
		String postUrl = this.baseUrl + "/iob/users";
		String getUrl = this.baseUrl + "/iob/users/login/{userDomain}/{userEmail}";

		NewUserBoundary newUserBoundary = Helper.getNewUserBoundary("ADMIN");
		UserBoundary userBoundary = this.restTemplate.postForObject(postUrl, newUserBoundary, UserBoundary.class);
		
		UserBoundary actual = this.restTemplate.getForObject(getUrl, UserBoundary.class, userBoundary.getUserId().getDomain(),
				userBoundary.getUserId().getEmail());
		assertThat(actual).isNotNull();
	}

	@Test
	@DisplayName("TEST: Update user")
	public void testUpdateUserOnServer() throws Exception {
		String postUrl = this.baseUrl + "/iob/users";
		String updateUrl = this.baseUrl + "/iob/users/{userDomain}/{userEmail}";
		String getUrl = this.baseUrl + "/iob/users/login/{userDomain}/{userEmail}";

		NewUserBoundary newUserBoundary = Helper.getNewUserBoundary("ADMIN");
		UserBoundary userBoundary = this.restTemplate.postForObject(postUrl, newUserBoundary, UserBoundary.class);
		userBoundary.setUsername("updateNewUserBoundary");
		this.restTemplate.put(updateUrl, userBoundary, userBoundary.getUserId().getDomain(),
				userBoundary.getUserId().getEmail());
		
		UserBoundary actual = this.restTemplate.getForObject(getUrl, UserBoundary.class, userBoundary.getUserId().getDomain(),
				userBoundary.getUserId().getEmail());

		assertThat(actual.getUsername().equals("updateNewUserBoundary"));

	}

	@Test
	@DisplayName("TEST: Delete all users")
	public void testDeleteAllUsersFromServer() throws Exception {
		String getUrl = this.baseUrl + "/iob/admin/users?userDomain={userDomain}&userEmail={userEmail}&size={size}&page={page}";
		String deleteUrl =this.baseUrl + "/iob/admin/users?userDomain={userDomain}&userEmail={userEmail}";
		
		this.restTemplate.postForObject(this.baseUrl + "/iob/users",
				Helper.getNewUserBoundary("MANAGER"), UserBoundary.class);
	
		this.restTemplate.postForObject(this.baseUrl + "/iob/users",
				Helper.getNewUserBoundary("PLAYER"), UserBoundary.class);
		
		UserBoundary userBoundaryAdmin = this.restTemplate.postForObject(this.baseUrl + "/iob/users",
				Helper.getNewUserBoundary("ADMIN"), UserBoundary.class);
		
		this.restTemplate.delete(deleteUrl,
				userBoundaryAdmin.getUserId().getDomain(),
				userBoundaryAdmin.getUserId().getEmail());
		
		userBoundaryAdmin = this.restTemplate.postForObject(this.baseUrl + "/iob/users",
				Helper.getNewUserBoundary("ADMIN"), UserBoundary.class);
		
		UserBoundary [] allUsers = this.restTemplate.getForObject(
				getUrl,
				UserBoundary[].class, userBoundaryAdmin.getUserId().getDomain(),
				userBoundaryAdmin.getUserId().getEmail(), 10, 0);
		
		assertThat(allUsers).isNotNull().hasSize(1);

	}
	
	@Test
	@DisplayName("TEST: GET all users - with a valid user that has manager role")
	public void testGetAllUsersFromServer() throws Exception {
		String getUrl = this.baseUrl + "/iob/admin/users?userDomain={userDomain}&userEmail={userEmail}&size={size}&page={page}";
		UserBoundary userBoundaryAdmin = this.restTemplate.postForObject(this.baseUrl + "/iob/users",
				Helper.getNewUserBoundary("ADMIN"), UserBoundary.class);
		
		this.restTemplate.postForObject(this.baseUrl + "/iob/users",
				Helper.getNewUserBoundary("MANAGER"), UserBoundary.class);
	
		this.restTemplate.postForObject(this.baseUrl + "/iob/users",
				Helper.getNewUserBoundary("PLAYER"), UserBoundary.class);
	
		UserBoundary [] allUsers = this.restTemplate.getForObject(
				getUrl,
				UserBoundary[].class, userBoundaryAdmin.getUserId().getDomain(),
				userBoundaryAdmin.getUserId().getEmail(), 10, 0);
		
		assertThat(allUsers).isNotNull().hasSize(3);

	}

}
