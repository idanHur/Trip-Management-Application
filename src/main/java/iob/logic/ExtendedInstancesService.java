package iob.logic;

import java.util.List;

import iob.restAPI.InstanceBoundary;

public interface ExtendedInstancesService extends InstancesService {
	public InstanceBoundary updateInstance(String instanceDomain, String instanceId, InstanceBoundary update,
			String userDomain, String userEmail);

	public InstanceBoundary getSpecificInstance(String instanceDomain, String instanceId, String userDomain,
			String userEmail);

	public List<InstanceBoundary> getAllInstances(String userDomain, String userEmail, int size, int page);

	public void deleteAllInstances(String userDomain, String userEmail);

	public List<InstanceBoundary> getAllInstanceByName(String name, String userDomain, String userEmail, int size,
			int page);

	public List<InstanceBoundary> getAllInstanceByType(String type, String userDomain, String userEmail, int size,
			int page);

	public List<InstanceBoundary> getAllInstanceByLocation(Double lat, Double lng, Double distance, String userDomain,
			String userEmail, int size, int page);
}
