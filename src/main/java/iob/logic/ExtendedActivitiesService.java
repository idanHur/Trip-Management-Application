package iob.logic;

import java.util.List;

import iob.restAPI.ActivityBoundary;

public interface ExtendedActivitiesService extends ActivitiesService {
	public List<ActivityBoundary> getAllActivities(String userDomain, String userEmail, int size, int page);

	public void deleteAllActivities(String userDomain, String userEmail);
}
