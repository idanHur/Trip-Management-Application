package iob.restAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import iob.logic.ExtendedActivitiesService;

@RestController
public class ActivityController {

	private ExtendedActivitiesService activityServices;

	@Autowired
	public ActivityController(ExtendedActivitiesService activityServices) {
		this.activityServices = activityServices;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/iob/admin/activities", produces = MediaType.APPLICATION_JSON_VALUE)
	public ActivityBoundary[] getAllActivities(@RequestParam(name = "userDomain", required = true) String userDomain,
			@RequestParam(name = "userEmail", required = true) String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {

		return this.activityServices.getAllActivities(userDomain, userEmail, size, page)
				.toArray(new ActivityBoundary[0]);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/iob/activities", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Object createActivity(@RequestBody ActivityBoundary boundary) {

		return this.activityServices.invokeActivity(boundary);
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/iob/admin/activities")
	public void deleteAllUsers(@RequestParam(name = "userDomain", required = true) String userDomain,
			@RequestParam(name = "userEmail", required = true) String userEmail) {

		this.activityServices.deleteAllActivities(userDomain, userEmail);
	}
}
