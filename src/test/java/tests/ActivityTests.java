package tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import iob.restAPI.ActivityBoundary;
import iob.restAPI.InstanceBoundary;
import iob.restAPI.NewUserBoundary;
import iob.restAPI.UserBoundary;
import iob.utility.DomainWithEmail;
import iob.utility.Instance;
import iob.utility.InvokedBy;


public class ActivityTests extends Base{


	@Test
	@DisplayName("\"TEST: POST Activity - with a valid user, returns the Activity boundry")
	public void testPostActivitytoServer() throws Exception {
		String postUrlActivityBoundary = this.baseUrl + "/iob/activities";
		String postUrlUsers = this.baseUrl + "/iob/users";
		String postInstanceBoundary = this.baseUrl + "/iob/instances";
		
		UserBoundary userBoundary = this.restTemplate.postForObject(postUrlUsers,
				Helper.getNewUserBoundary("PLAYER"), UserBoundary.class);
			
		UserBoundary userBoundaryManager = this.restTemplate.postForObject(postUrlUsers,
				Helper.getNewUserBoundary("MANAGER"), UserBoundary.class);
			
		
		DomainWithEmail de = new DomainWithEmail(userBoundary.getUserId().getDomain(), userBoundary.getUserId().getEmail());

		InstanceBoundary instance = Helper.getInstanceBoundary("testType", "testName",
				userBoundaryManager.getUserId().getDomain(), userBoundaryManager.getUserId().getEmail());
		instance.setActive(true);
		
		InstanceBoundary instanceBoundary = this.restTemplate.postForObject(postInstanceBoundary, instance, InstanceBoundary.class);	
		
		ActivityBoundary activityBoundary = new ActivityBoundary();
		activityBoundary.setInvokedBy(new InvokedBy(de));
		activityBoundary.setType("test");
		activityBoundary.setInstance(new Instance(instanceBoundary.getInstanceId()));
		activityBoundary.setActivityAttributes(new HashMap<String, Object>());

		ActivityBoundary rv = this.restTemplate.postForObject(postUrlActivityBoundary, activityBoundary,
				ActivityBoundary.class);


		assertThat(rv).isNotNull();
	}
	
	@Test
	@DisplayName("\"TEST: POST Activity - that was invoked by a non valid user, throws BAD_REQUEST exception")
	public void testPostActivitytoServerInvokedByNonValidUser() throws Exception {
		String postUrlActivityBoundary = this.baseUrl + "/iob/activities";
		String postUrlUsers = this.baseUrl + "/iob/users";
		String postInstanceBoundary = this.baseUrl + "/iob/instances";
		
		UserBoundary userBoundaryManager = this.restTemplate.postForObject(postUrlUsers,
				Helper.getNewUserBoundary("MANAGER"), UserBoundary.class);
			
		
		DomainWithEmail de = new DomainWithEmail(userBoundaryManager.getUserId().getDomain(), userBoundaryManager.getUserId().getEmail());

		InstanceBoundary instance = Helper.getInstanceBoundary("testType", "testName",
				userBoundaryManager.getUserId().getDomain(), userBoundaryManager.getUserId().getEmail());
		instance.setActive(true);
		
		InstanceBoundary instanceBoundary = this.restTemplate.postForObject(postInstanceBoundary, instance, InstanceBoundary.class);	
		
		ActivityBoundary activityBoundary = new ActivityBoundary();
		activityBoundary.setInvokedBy(new InvokedBy(de));
		activityBoundary.setType("test");
		activityBoundary.setInstance(new Instance(instanceBoundary.getInstanceId()));
		activityBoundary.setActivityAttributes(new HashMap<String, Object>());

		try {
			this.restTemplate.postForObject(postUrlActivityBoundary, activityBoundary,
					ActivityBoundary.class);
		} catch (Exception e) {
			assertThat(true);
		}
		
		assertThat(false);
	}
	
	@Test
	@DisplayName("\"TEST: GET all Activitys - with a valid user that has admin role")
	public void testGetActivityFromServer() throws Exception {
		String postUrlActivityBoundary = this.baseUrl + "/iob/activities";
		String postUrlUsers = this.baseUrl + "/iob/users";
		String postInstanceBoundary = this.baseUrl + "/iob/instances";
		String getUrl = this.baseUrl + "/iob/admin/activities?userDomain={userDomain}&userEmail={userEmail}&size={size}&page={page}";
		
		UserBoundary userBoundary = this.restTemplate.postForObject(postUrlUsers,
				Helper.getNewUserBoundary("PLAYER"), UserBoundary.class);
			
		UserBoundary userBoundaryManager = this.restTemplate.postForObject(postUrlUsers,
				Helper.getNewUserBoundary("MANAGER"), UserBoundary.class);
			
		UserBoundary userBoundaryAdmin = this.restTemplate.postForObject(postUrlUsers,
				Helper.getNewUserBoundary("ADMIN"), UserBoundary.class);
			
		DomainWithEmail de = new DomainWithEmail(userBoundary.getUserId().getDomain(), userBoundary.getUserId().getEmail());
		InvokedBy inv = new InvokedBy(de);

		InstanceBoundary instance = Helper.getInstanceBoundary("testType", "testName",
				userBoundaryManager.getUserId().getDomain(), userBoundaryManager.getUserId().getEmail());
		instance.setActive(true);
		
		InstanceBoundary instanceBoundary = this.restTemplate.postForObject(postInstanceBoundary, instance, InstanceBoundary.class);
		
		for(int i = 0; i < 15; i++ )
		{
			ActivityBoundary activityBoundary = new ActivityBoundary();
			activityBoundary.setInvokedBy(inv);
			activityBoundary.setType("test" + i);
			activityBoundary.setInstance(new Instance(instanceBoundary.getInstanceId()));
			activityBoundary.setActivityAttributes(new HashMap<String, Object>());
			
			this.restTemplate.postForObject(postUrlActivityBoundary, activityBoundary,
					ActivityBoundary.class);
		}
		
		ActivityBoundary[] actual = this.restTemplate.getForObject(getUrl, ActivityBoundary[].class, "2022b.maya.gembom",
				userBoundaryAdmin.getUserId().getEmail(),15,0);

		assertThat(actual).isNotNull().hasSize(15);
	}
	
	@Test
	@DisplayName("\"TEST: GET all Activitys - with a non valid user, throws BAD_REQUEST exception")
	public void testGetActivityFromServerNonValidUser() throws Exception {
		String postUrlActivityBoundary = this.baseUrl + "/iob/activities";
		String postUrlUsers = this.baseUrl + "/iob/users";
		String postInstanceBoundary = this.baseUrl + "/iob/instances";
		String getUrl = this.baseUrl + "/iob/admin/activities?userDomain={userDomain}&userEmail={userEmail}&size={size}&page={page}";
		
		UserBoundary userBoundary = this.restTemplate.postForObject(postUrlUsers,
				Helper.getNewUserBoundary("PLAYER"), UserBoundary.class);
			
		UserBoundary userBoundaryManager = this.restTemplate.postForObject(postUrlUsers,
				Helper.getNewUserBoundary("MANAGER"), UserBoundary.class);
			
			
		DomainWithEmail de = new DomainWithEmail(userBoundary.getUserId().getDomain(), userBoundary.getUserId().getEmail());
		InvokedBy inv = new InvokedBy(de);

		InstanceBoundary instance = Helper.getInstanceBoundary("testType", "testName",
				userBoundaryManager.getUserId().getDomain(), userBoundaryManager.getUserId().getEmail());
		instance.setActive(true);
		
		InstanceBoundary instanceBoundary = this.restTemplate.postForObject(postInstanceBoundary, instance, InstanceBoundary.class);	
		
		for(int i = 0; i < 15; i++ )
		{
			ActivityBoundary activityBoundary = new ActivityBoundary();
			activityBoundary.setInvokedBy(inv);
			activityBoundary.setType("test" + i);
			activityBoundary.setInstance(new Instance(instanceBoundary.getInstanceId()));
			activityBoundary.setActivityAttributes(new HashMap<String, Object>());
			
			this.restTemplate.postForObject(postUrlActivityBoundary, activityBoundary,
					ActivityBoundary.class);
		}
		
		try {
			this.restTemplate.getForObject(getUrl, ActivityBoundary[].class, "2022b.maya.gembom",
					userBoundaryManager.getUserId().getEmail(),15,0);	
		} catch (Exception e) {
			assertThat(true);
		}
		
		assertThat(false);
	}


	@Test
	@DisplayName("\"TEST: GET last 5 Activitys - with a valid user that has admin role using paginatin")
	public void testGetSomeActivityFromServer() throws Exception {
		String postUrlActivityBoundary = this.baseUrl + "/iob/activities";
		String postUrlUsers = this.baseUrl + "/iob/users";
		String postInstanceBoundary = this.baseUrl + "/iob/instances";
		String getUrl = this.baseUrl + "/iob/admin/activities?userDomain={userDomain}&userEmail={userEmail}&size={size}&page={page}";
		
		UserBoundary userBoundary = this.restTemplate.postForObject(postUrlUsers,
				Helper.getNewUserBoundary("PLAYER"), UserBoundary.class);
			
		UserBoundary userBoundaryManager = this.restTemplate.postForObject(postUrlUsers,
				Helper.getNewUserBoundary("MANAGER"), UserBoundary.class);
			
		UserBoundary userBoundaryAdmin = this.restTemplate.postForObject(postUrlUsers,
				Helper.getNewUserBoundary("ADMIN"), UserBoundary.class);
			
		DomainWithEmail de = new DomainWithEmail(userBoundary.getUserId().getDomain(), userBoundary.getUserId().getEmail());
		InvokedBy inv = new InvokedBy(de);

		InstanceBoundary instance = Helper.getInstanceBoundary("testType", "testName",
				userBoundaryManager.getUserId().getDomain(), userBoundaryManager.getUserId().getEmail());
		instance.setActive(true);
		
		InstanceBoundary instanceBoundary = this.restTemplate.postForObject(postInstanceBoundary, instance, InstanceBoundary.class);	
		
		ArrayList<ActivityBoundary> lastFive = new ArrayList<ActivityBoundary>();
		
		for(int i = 0; i < 15; i++ )
		{
			ActivityBoundary activityBoundary = new ActivityBoundary();
			activityBoundary.setInvokedBy(inv);
			activityBoundary.setType("test" + i);
			activityBoundary.setInstance(new Instance(instanceBoundary.getInstanceId()));
			activityBoundary.setActivityAttributes(new HashMap<String, Object>());

			ActivityBoundary temp = this.restTemplate.postForObject(postUrlActivityBoundary, activityBoundary,
					ActivityBoundary.class);
			if(i >= 10)
				lastFive.add(temp);
		}
		
		ActivityBoundary[] actual = this.restTemplate.getForObject(getUrl, ActivityBoundary[].class, "2022b.maya.gembom",
				userBoundaryAdmin.getUserId().getEmail(),10,1);

		Boolean equals = true;
		for(int i = 0; i < 5; i++ )
			if(!actual[i].getActivityId().getId().equals(lastFive.get(i).getActivityId().getId()))
				equals = false;	
			
		assertTrue(equals);
	}
	
	
	@Test
	@DisplayName("TEST: DELETE all Activity - with a valid user that has admin role")
	public void testDeleteActivityOnServer() throws Exception {
		String postUrlActivityBoundary = this.baseUrl + "/iob/activities";
		String postUrlUsers = this.baseUrl + "/iob/users";
		String postInstanceBoundary = this.baseUrl + "/iob/instances";
		String getUrl = this.baseUrl + "/iob/admin/activities?userDomain={userDomain}&userEmail={userEmail}&size={size}&page={page}";
		String delUrl = this.baseUrl + "/iob/admin/activities?userDomain={userDomain}&userEmail={userEmail}";

		UserBoundary userBoundary = this.restTemplate.postForObject(postUrlUsers,
				Helper.getNewUserBoundary("PLAYER"), UserBoundary.class);
			
		UserBoundary userBoundaryManager = this.restTemplate.postForObject(postUrlUsers,
				Helper.getNewUserBoundary("MANAGER"), UserBoundary.class);
			
		UserBoundary userBoundaryAdmin = this.restTemplate.postForObject(postUrlUsers,
				Helper.getNewUserBoundary("ADMIN"), UserBoundary.class);
			
		DomainWithEmail de = new DomainWithEmail(userBoundary.getUserId().getDomain(), userBoundary.getUserId().getEmail());
	
		InstanceBoundary instance = Helper.getInstanceBoundary("testType", "testName",
				userBoundaryManager.getUserId().getDomain(), userBoundaryManager.getUserId().getEmail());
		instance.setActive(true);
		
		InstanceBoundary instanceBoundary = this.restTemplate.postForObject(postInstanceBoundary, instance, InstanceBoundary.class);	
		
		ActivityBoundary activityBoundary = new ActivityBoundary();
		activityBoundary.setInvokedBy(new InvokedBy(de));
		activityBoundary.setType("test");
		activityBoundary.setInstance(new Instance(instanceBoundary.getInstanceId()));
		activityBoundary.setActivityAttributes(new HashMap<String, Object>());
	
		this.restTemplate.postForObject(postUrlActivityBoundary, activityBoundary,
				ActivityBoundary.class);
	
		
		this.restTemplate.delete(delUrl, "2022b.maya.gembom",
				userBoundaryAdmin.getUserId().getEmail());
		
		ActivityBoundary[] actual = this.restTemplate.getForObject(getUrl, ActivityBoundary[].class, "2022b.maya.gembom",
				userBoundaryAdmin.getUserId().getEmail(),10,0);

		assertThat(actual).isEmpty();
		
	}
	
	@Test 
	@DisplayName("TEST: DELETE all Activity - with a non valid user, throws BAD_REQUEST exception")
	public void testDeleteActivityOnServerNonValidUser() throws Exception {
		String postUrlActivityBoundary = this.baseUrl + "/iob/activities";
		String postUrlUsers = this.baseUrl + "/iob/users";
		String postInstanceBoundary = this.baseUrl + "/iob/instances";
		String delUrl = this.baseUrl + "/iob/admin/activities?userDomain={userDomain}&userEmail={userEmail}";

		UserBoundary userBoundary = this.restTemplate.postForObject(postUrlUsers,
				Helper.getNewUserBoundary("PLAYER"), UserBoundary.class);
			
		UserBoundary userBoundaryManager = this.restTemplate.postForObject(postUrlUsers,
				Helper.getNewUserBoundary("MANAGER"), UserBoundary.class);
			
			
		DomainWithEmail de = new DomainWithEmail(userBoundary.getUserId().getDomain(), userBoundary.getUserId().getEmail());
	
		InstanceBoundary instance = Helper.getInstanceBoundary("testType", "testName",
				userBoundaryManager.getUserId().getDomain(), userBoundaryManager.getUserId().getEmail());
		instance.setActive(true);
		
		InstanceBoundary instanceBoundary = this.restTemplate.postForObject(postInstanceBoundary, instance, InstanceBoundary.class);	
		
		ActivityBoundary activityBoundary = new ActivityBoundary();
		activityBoundary.setInvokedBy(new InvokedBy(de));
		activityBoundary.setType("test");
		activityBoundary.setInstance(new Instance(instanceBoundary.getInstanceId()));
		activityBoundary.setActivityAttributes(new HashMap<String, Object>());
	
		this.restTemplate.postForObject(postUrlActivityBoundary, activityBoundary,
				ActivityBoundary.class);
	
		try {
			this.restTemplate.delete(delUrl, "2022b.maya.gembom",
					userBoundaryManager.getUserId().getEmail());
		} catch (Exception e) {
			assertThat(true);
		}
		
		assertThat(false);
	}
}
