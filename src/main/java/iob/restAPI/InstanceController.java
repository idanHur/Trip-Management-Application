package iob.restAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import iob.logic.ExtendedInstancesService;

@RestController
public class InstanceController {

	private ExtendedInstancesService instanceServices;

	@Autowired
	public InstanceController(ExtendedInstancesService instanceServices) {
		this.instanceServices = instanceServices;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/iob/instances/{instanceDomain}/{instanceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public InstanceBoundary retrieveInstance(
			@PathVariable("instanceDomain") String instanceDomain,
			@PathVariable("instanceId") String instanceId,
			@RequestParam(name = "userDomain", required = true) String userDomain,
			@RequestParam(name = "userEmail", required = true) String userEmail) {

		return this.instanceServices.getSpecificInstance(instanceDomain, instanceId, userDomain, userEmail);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/iob/instances",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public InstanceBoundary[] getAllInstances(
			@RequestParam(name = "userDomain", required = true) String userDomain,
			@RequestParam(name = "userEmail", required = true) String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		return this.instanceServices.getAllInstances(userDomain, userEmail, size, page)
				.toArray(new InstanceBoundary[0]);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/iob/instances", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public InstanceBoundary createInstance(@RequestBody InstanceBoundary boundary) {
		return this.instanceServices.createInstance(boundary);
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/iob/instances/{instanceDomain}/{instanceId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void UpdateInstace(
			@PathVariable("instanceDomain") String instanceDomain,
			@PathVariable("instanceId") String instanceId,
			@RequestParam(name = "userDomain", required = true) String userDomain,
			@RequestParam(name = "userEmail", required = true) String userEmail,
			@RequestBody InstanceBoundary updateBoundary) {

		this.instanceServices.updateInstance(instanceDomain, instanceId, updateBoundary, userDomain, userEmail);
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/iob/admin/instances")
	public void deleteAllInstances(
			@RequestParam(name = "userDomain", required = true) String userDomain,
			@RequestParam(name = "userEmail", required = true) String userEmail) {

		this.instanceServices.deleteAllInstances(userDomain, userEmail);
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/iob/instances/search/byName/{name}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public InstanceBoundary[] getAllInstancesByName(
			@PathVariable("name") String name,
			@RequestParam(name = "userDomain", required = true) String userDomain,
			@RequestParam(name = "userEmail", required = true) String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		return this.instanceServices.getAllInstanceByName(name, userDomain, userEmail, size, page)
				.toArray(new InstanceBoundary[0]);
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/iob/instances/search/byType/{type}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public InstanceBoundary[] getAllInstancesByType(
			@PathVariable("type") String type,
			@RequestParam(name = "userDomain", required = true) String userDomain,
			@RequestParam(name = "userEmail", required = true) String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		return this.instanceServices.getAllInstanceByType(type, userDomain, userEmail, size, page)
				.toArray(new InstanceBoundary[0]);
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/iob/instances/search/near/{lat}/{lng}/{distance}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public InstanceBoundary[] getAllInstancesByLocation(
			@PathVariable("lat") Double lat,
			@PathVariable("lng") Double lng,
			@PathVariable("distance") Double distance,
			@RequestParam(name = "userDomain", required = true) String userDomain,
			@RequestParam(name = "userEmail", required = true) String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		return this.instanceServices.getAllInstanceByLocation(lat, lng, distance, userDomain, userEmail, size, page)
				.toArray(new InstanceBoundary[0]);
	}
}
