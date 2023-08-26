package tests;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import iob.Application;
import iob.restAPI.UserBoundary;

@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class Base {
	
	protected int port;
	protected RestTemplate restTemplate;
	protected String baseUrl;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {
		this.baseUrl = "http://localhost:" + this.port;
		this.restTemplate = new RestTemplate();
	}
	
	@BeforeEach
	public void setUp() {
		UserBoundary userBoundaryAdmin = this.restTemplate.postForObject(this.baseUrl + "/iob/users",
				Helper.getNewUserBoundary("ADMIN"), UserBoundary.class);
		
		this.restTemplate.delete(this.baseUrl + "/iob/admin/instances?userDomain={userDomain}&userEmail={userEmail}",
				userBoundaryAdmin.getUserId().getDomain(),
				userBoundaryAdmin.getUserId().getEmail());
		
		this.restTemplate.delete(this.baseUrl + "/iob/admin/activities?userDomain={userDomain}&userEmail={userEmail}",
				userBoundaryAdmin.getUserId().getDomain(),
				userBoundaryAdmin.getUserId().getEmail());
		
		this.restTemplate.delete(this.baseUrl + "/iob/admin/users?userDomain={userDomain}&userEmail={userEmail}",
				userBoundaryAdmin.getUserId().getDomain(),
				userBoundaryAdmin.getUserId().getEmail());
	}
	
//	@AfterEach
//	public void tearDown() {
//		this.restTemplate.delete(this.baseUrl);
//	}
	
}
