package iob.restAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import iob.logic.ExtendedUsersService;
import iob.utility.DomainWithEmail;

@RestController
public class UserController {
	
	private ExtendedUsersService usersService;
	
	@Autowired
	public UserController(ExtendedUsersService userServices) {
		this.usersService = userServices;
	}

	@RequestMapping(
			method = RequestMethod.GET,
			path = "/iob/users/login/{userDomain}/{userEmail}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary loginUser(
			@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail) {
		
		return this.usersService.login(userDomain, userEmail);
	}
	
	@RequestMapping(
			method = RequestMethod.GET,
			path = "/iob/admin/users",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] getAllUsers(
			@RequestParam(name = "userDomain", required = true) String userDomain,
			@RequestParam(name = "userEmail", required = true) String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		
		return this.usersService.getAllUsers(userDomain, userEmail, size, page).toArray(new UserBoundary[0]);
	}
	
	@RequestMapping(
			method = RequestMethod.POST,
			path = "/iob/users",
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
		public UserBoundary createUser (@RequestBody NewUserBoundary boundary) {
		
		UserBoundary userBoundary = new UserBoundary();
		userBoundary.setUserId(new DomainWithEmail(null, boundary.getEmail()));
		userBoundary.setAvatar(boundary.getAvatar());
		userBoundary.setRole(boundary.getRole());
		userBoundary.setUsername(boundary.getUsername());
		
		return this.usersService.createUser(userBoundary);
		}
	
	@RequestMapping(
			method = RequestMethod.PUT,
			path = "/iob/users/{userDomain}/{userEmail}",
			consumes = MediaType.APPLICATION_JSON_VALUE)
		public void UpdateUser (
				@PathVariable("userDomain") String userDomain, 
				@PathVariable("userEmail") String userEmail, 
				@RequestBody UserBoundary updateBoundary) {
		
			this.usersService.updateUser(userDomain, userEmail, updateBoundary);
			
		}
	
	@RequestMapping(
			method = RequestMethod.DELETE,
			path = "/iob/admin/users")
		public void deleteAllUsers (
				@RequestParam(name = "userDomain", required = true) String userDomain,
				@RequestParam(name = "userEmail", required = true) String userEmail) {
		
			this.usersService.deleteAllUsers(userDomain, userEmail);
		}
}
