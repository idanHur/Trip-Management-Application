package iob.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iob.data.ActivityCrud;
import iob.data.InstanceCrud;
import iob.data.InstanceEntity;
import iob.data.UserCrud;
import iob.data.UserRole;
import iob.logic.exceptions.BadRequestException;
import iob.logic.exceptions.DeprecatedMethodException;
import iob.logic.exceptions.ObjNotFoundException;
import iob.restAPI.InstanceBoundary;
import iob.utility.DomainWithEmail;
import iob.utility.DomainWithId;
import iob.utility.InstanceConvertor;

@Service
public class InstancesServiceJpa extends ServiceJpa implements ExtendedInstancesService {

	private InstanceConvertor instanceConvertor;

	@Autowired
	public InstancesServiceJpa(InstanceCrud instanceCrud, UserCrud userCrud, ActivityCrud activityCrud,
			InstanceConvertor instanceConvertor) {
		super(userCrud, activityCrud, instanceCrud);
		this.instanceConvertor = instanceConvertor;
	}

	@Override
	@Transactional(readOnly = false)
	public InstanceBoundary createInstance(InstanceBoundary instanceBoundary) {

		instanceBoundary.setInstanceId(new DomainWithId(configurableDomain, UUID.randomUUID().toString()));
		if (instanceBoundary.getCreatedTimestamp() == null
				|| instanceBoundary.getCreatedTimestamp().toString().isEmpty())
			instanceBoundary.setCreatedTimestamp(new Date());

		if (!checkUserIdInDB(instanceBoundary.getCreatedBy().getUserId()))
			throw new BadRequestException();

		DomainWithEmail de = instanceBoundary.getCreatedBy().getUserId();
		if (getUserRoleInDB(de.getDomain(), de.getEmail()) != UserRole.MANAGER)
			throw new BadRequestException();

		InstanceEntity instanceEntity = instanceConvertor.toEntity(instanceBoundary);
		instanceCrud.save(instanceEntity);
		return instanceConvertor.toBoundary(instanceEntity);
	}

	@Override
	public InstanceBoundary updateInstance(String instanceDomain, String instanceId, InstanceBoundary update) {
		throw new DeprecatedMethodException("deprecated method - use updateInstance with user domin and user email");
	}

	@Override
	@Transactional(readOnly = false)
	public InstanceBoundary updateInstance(String instanceDomain, String instanceId, InstanceBoundary update,
			String userDomain, String userEmail) {
		UserRole userRole = getUserRoleInDB(userDomain, userEmail);
		if (userRole == null)
			throw new BadRequestException();

		if (userRole != UserRole.MANAGER)
			throw new BadRequestException();

		if (instanceDomain == null || instanceDomain.isEmpty())
			throw new BadRequestException();
		if (instanceId == null || instanceId.isEmpty())
			throw new BadRequestException();
		if (update == null)
			throw new BadRequestException();

		InstanceEntity instanceEntity = getInstanceEntityById(instanceDomain, instanceId);
		instanceEntity = instanceConvertor.updateEntityByBoundary(instanceEntity, update);

		instanceCrud.save(instanceEntity);

		return instanceConvertor.toBoundary(instanceEntity);
	}

	@Override
	public InstanceBoundary getSpecificInstance(String instanceDomain, String instanceId) {
		throw new DeprecatedMethodException(
				"deprecated method - use getSpecificInstance with user domin and user email");
	}

	@Override
	@Transactional(readOnly = true)
	public InstanceBoundary getSpecificInstance(String instanceDomain, String instanceId, String userDomain,
			String userEmail) {
		UserRole userRole = getUserRoleInDB(userDomain, userEmail);
		if (userRole == null)
			throw new BadRequestException();

		InstanceBoundary instanceBoundary = instanceConvertor
				.toBoundary(getInstanceEntityById(instanceDomain, instanceId));

		if (userRole == UserRole.MANAGER)
			return instanceBoundary;

		if (userRole == UserRole.PLAYER)
			if (instanceBoundary.getActive() != null && instanceBoundary.getActive())
				return instanceBoundary;
			else
				throw new ObjNotFoundException();

		throw new BadRequestException();
	}

	@Override
	public List<InstanceBoundary> getAllInstances() {
		throw new DeprecatedMethodException("deprecated method - use getAllInstances with paginayion instead");
	}

	@Override
	@Transactional(readOnly = true)
	public List<InstanceBoundary> getAllInstances(String userDomain, String userEmail, int size, int page) {
		UserRole userRole = getUserRoleInDB(userDomain, userEmail);
		if (userRole == null)
			throw new BadRequestException();

		if (userRole == UserRole.MANAGER)
			return this.instanceCrud.findAll(PageRequest.of(page, size, Direction.ASC, "instanceId")).stream()
					.map(this.instanceConvertor::toBoundary).collect(Collectors.toList());
		;

		if (userRole == UserRole.PLAYER) {
			return this.instanceCrud
					.findAllByActive(true, PageRequest.of(page, size, Direction.ASC, "instanceId"))
					.stream().map(this.instanceConvertor::toBoundary).collect(Collectors.toList());

		}
		throw new BadRequestException();
	}

	@Override
	public void deleteAllInstances() {
		throw new DeprecatedMethodException(
				"deprecated method - use deleteAllInstances with user domin and user email");
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteAllInstances(String userDomain, String userEmail) {
		UserRole userRole = getUserRoleInDB(userDomain, userEmail);
		if (userRole == null)
			throw new BadRequestException();
		if (userRole == UserRole.ADMIN) {
			instanceCrud.deleteAll();
			return;
		}
		throw new BadRequestException();
	}

	@Override
	@Transactional(readOnly = true)
	public List<InstanceBoundary> getAllInstanceByName(String name, String userDomain, String userEmail, int size,
			int page) {
		UserRole userRole = getUserRoleInDB(userDomain, userEmail);
		if (userRole == null)
			throw new BadRequestException();

		if (userRole == UserRole.MANAGER)
			return this.instanceCrud
					.findAllByName(name, PageRequest.of(page, size, Direction.ASC, "instanceId"))
					.stream().map(this.instanceConvertor::toBoundary).collect(Collectors.toList());

		if (userRole == UserRole.PLAYER) {
			return this.instanceCrud
					.findAllByNameAndActive(name, true,
							PageRequest.of(page, size, Direction.ASC, "instanceId"))
					.stream().map(this.instanceConvertor::toBoundary).collect(Collectors.toList());
		}
		throw new BadRequestException();
	}

	@Override
	@Transactional(readOnly = true)
	public List<InstanceBoundary> getAllInstanceByType(String type, String userDomain, String userEmail, int size,
			int page) {
		UserRole userRole = getUserRoleInDB(userDomain, userEmail);
		if (userRole == null)
			throw new BadRequestException();

		if (userRole == UserRole.MANAGER)
			return this.instanceCrud
					.findAllByType(type, PageRequest.of(page, size, Direction.ASC, "instanceId"))
					.stream().map(this.instanceConvertor::toBoundary).collect(Collectors.toList());

		if (userRole == UserRole.PLAYER) {
			return this.instanceCrud
					.findAllByTypeAndActive(type, true,
							PageRequest.of(page, size, Direction.ASC, "instanceId"))
					.stream().map(this.instanceConvertor::toBoundary).collect(Collectors.toList());
		}
		throw new BadRequestException();
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<InstanceBoundary> getAllInstanceByLocation(Double lat, Double lng, Double distance, String userDomain,
			String userEmail, int size, int page) {

		UserRole userRole = getUserRoleInDB(userDomain, userEmail);
		if (userRole == null)
			throw new BadRequestException();

			if (userRole == UserRole.MANAGER)
				return this.instanceCrud.findAllByLocationLatBetweenAndLocationLngBetween(lat-distance, lat+distance, lng-distance, lng+distance,
						PageRequest.of(page, size, Direction.ASC, "instanceId"))
				.stream().map(this.instanceConvertor::toBoundary).collect(Collectors.toList());
			else if (userRole == UserRole.PLAYER)
				return this.instanceCrud.findAllByLocationLatBetweenAndLocationLngBetweenAndActive(lat-distance, lat+distance, lng-distance, lng+distance, true
						,PageRequest.of(page, size, Direction.ASC, "instanceId"))
				.stream().map(this.instanceConvertor::toBoundary).collect(Collectors.toList());

		throw new BadRequestException();
	}

	private boolean calculateDistance(InstanceBoundary instanceBoundary, Double lat, Double lng, Double distance) {
		double lat1 = instanceBoundary.getLocation().getLat();
		double lng1 = instanceBoundary.getLocation().getLng();
		double dis = Math.sqrt(Math.pow(lat - lat1, 2) + Math.pow(lng - lng1, 2));
		return dis <= distance;
	}

	@Transactional(readOnly = true)
	private InstanceEntity getInstanceEntityById(String instanceDomain, String instanceId) {
		String id = instanceDomain + "_" + instanceId;
		Optional<InstanceEntity> op = instanceCrud.findById(id);
		if (op.isPresent()) {
			return op.get();
		} else {
			throw new ObjNotFoundException(
					"Could not find instance by id: " + instanceId + " and by domain: " + instanceDomain);
		}
	}

}
