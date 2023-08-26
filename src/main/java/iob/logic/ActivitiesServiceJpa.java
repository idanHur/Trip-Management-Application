package iob.logic;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iob.data.ActivityCrud;
import iob.data.ActivityEntity;
import iob.data.InstanceCrud;
import iob.data.UserCrud;
import iob.data.UserRole;
import iob.logic.exceptions.BadRequestException;
import iob.logic.exceptions.DeprecatedMethodException;
import iob.restAPI.ActivityBoundary;
import iob.utility.ActivityConvertor;
import iob.utility.DomainWithEmail;
import iob.utility.DomainWithId;

@Service
public class ActivitiesServiceJpa extends ServiceJpa implements ExtendedActivitiesService {

	private ActivityConvertor activityConvertor;

	@Autowired
	public ActivitiesServiceJpa(InstanceCrud instanceCrud, UserCrud userCrud, ActivityCrud activityCrud,
			ActivityConvertor activityConvertor) {
		super(userCrud, activityCrud, instanceCrud);
		this.activityConvertor = activityConvertor;
	}

	@Override
	@Transactional(readOnly = false)
	public Object invokeActivity(ActivityBoundary activityBoundary) {
		activityBoundary.setActivityId(new DomainWithId(configurableDomain, UUID.randomUUID().toString()));
		if (activityBoundary.getCreatedTimestamp() == null
				|| activityBoundary.getCreatedTimestamp().toString().isEmpty())
			activityBoundary.setCreatedTimestamp(new Date());

		if (!checkUserIdInDB(activityBoundary.getInvokedBy().getUserId()))
			throw new BadRequestException();
		
		if (!getInstanceActiveInDB(activityBoundary.getInstance().getInstanceId()))
			throw new BadRequestException();
		
		DomainWithEmail de = activityBoundary.getInvokedBy().getUserId();
		if (getUserRoleInDB(de.getDomain(),de.getEmail()) != UserRole.PLAYER)
			throw new BadRequestException();

		ActivityEntity activityEntity = activityConvertor.toEntity(activityBoundary);
		activityCrud.save(activityEntity);

		return activityConvertor.toBoundary(activityEntity);
	}

	@Override
	public List<ActivityBoundary> getAllActivities() {
		throw new DeprecatedMethodException(
				"deprecated method - use getAllActivities with with paginayion instead");
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ActivityBoundary> getAllActivities(String userDomain, String userEmail, int size, int page) {
		UserRole userRole = getUserRoleInDB(userDomain, userEmail);
		if (userRole == null)
			throw new BadRequestException();
		
		if (userRole != UserRole.ADMIN)
			throw new BadRequestException();
		
		List<ActivityBoundary> allActivityBoundaries = this.activityCrud
				.findAll(PageRequest.of(page, size, Direction.ASC, "createdTimestamp", "acticityId"))
				.stream()
				.map(this.activityConvertor::toBoundary).collect(Collectors.toList());

		return allActivityBoundaries;
	}

	@Override
	public void deleteAllActivities() {
		throw new DeprecatedMethodException(
				"deprecated method - use deleteAllActivities with user domin and user email");
	}
	
	@Override
	@Transactional(readOnly = false)
	public void deleteAllActivities(String userDomain, String userEmail) {
		UserRole userRole = getUserRoleInDB(userDomain, userEmail);
		if (userRole == null)
			throw new BadRequestException();
		if (userRole == UserRole.ADMIN) {
			activityCrud.deleteAll();
			return;
		}
		throw new BadRequestException();
	}
}
