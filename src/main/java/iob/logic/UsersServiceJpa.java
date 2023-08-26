package iob.logic;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iob.data.ActivityCrud;
import iob.data.InstanceCrud;
import iob.data.UserCrud;
import iob.data.UserEntity;
import iob.data.UserRole;
import iob.logic.exceptions.BadRequestException;
import iob.logic.exceptions.DeprecatedMethodException;
import iob.logic.exceptions.ObjNotFoundException;
import iob.restAPI.UserBoundary;
import iob.utility.UserConverter;

@Service
public class UsersServiceJpa extends ServiceJpa implements ExtendedUsersService {

	private UserConverter userConverter;

	@Autowired
	public UsersServiceJpa(InstanceCrud instanceCrud, UserCrud userCrud, ActivityCrud activityCrud,
			UserConverter userConverter) {
		super(userCrud, activityCrud, instanceCrud);
		this.userConverter = userConverter;
	}

	@Override
	@Transactional(readOnly = false)
	public UserBoundary createUser(UserBoundary userToStore) {
		userToStore.getUserId().setDomain(configurableDomain);
		UserEntity entity = this.userConverter.toEntity(userToStore);
		entity = this.userCrud.save(entity);
		return this.userConverter.toBoundary(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public UserBoundary login(String userDomain, String userEmail) {
		return this.userConverter.toBoundary(getUserEntityById(userDomain, userEmail));
	}

	@Override
	@Transactional(readOnly = false)
	public UserBoundary updateUser(String userDomain, String userEmail, UserBoundary update) {
		if (userDomain == null || userDomain.isEmpty())
			throw new BadRequestException();
		if (userEmail == null || userEmail.isEmpty())
			throw new BadRequestException();
		if (update == null)
			throw new BadRequestException();

		UserEntity userEntity = getUserEntityById(userDomain, userEmail);
		userEntity = userConverter.updateEntityByBoundary(userEntity, update);

		userCrud.save(userEntity);

		return userConverter.toBoundary(userEntity);
	}

	@Override
	public List<UserBoundary> getAllUsers() {
		throw new DeprecatedMethodException("deprecated method - use deleteAllUsers with user domin and user email");
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> getAllUsers(String userDomain, String userEmail, int size, int page) {
		UserRole userRole = getUserRoleInDB(userDomain, userEmail);
		if (userRole == null)
			throw new BadRequestException();

		if (userRole != UserRole.ADMIN)
			throw new BadRequestException();

		List<UserBoundary> allUserBoundaries = this.userCrud
				.findAll(PageRequest.of(page, size, Direction.ASC, "userId")).stream()
				.map(this.userConverter::toBoundary).collect(Collectors.toList());

		return allUserBoundaries;
	}

	@Override
	public void deleteAllUsers() {
		throw new DeprecatedMethodException("deprecated method - use deleteAllUsers with user domin and user email");
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteAllUsers(String userDomain, String userEmail) {
		UserRole userRole = getUserRoleInDB(userDomain, userEmail);
		if (userRole == null)
			throw new BadRequestException();
		if (userRole == UserRole.ADMIN) {
			userCrud.deleteAll();
			return;
		}
		throw new BadRequestException();
	}

	@Transactional(readOnly = true)
	private UserEntity getUserEntityById(String userDomain, String userEmail) {
		Optional<UserEntity> optionalUser = this.userCrud.findById(userDomain + "_" + userEmail);
		if (optionalUser.isPresent()) {
			return optionalUser.get();
		} else {
			throw new ObjNotFoundException(
					"Could not find user by mail: " + userEmail + " and by domain: " + userDomain);
		}
	}

}
