package iob.logic;

import java.util.List;

import iob.restAPI.ActivityBoundary;

public interface ActivitiesService {
	public Object invokeActivity(ActivityBoundary activityBoundary);

	@Deprecated
	public List<ActivityBoundary> getAllActivities();

	@Deprecated
	public void deleteAllActivities();
}
