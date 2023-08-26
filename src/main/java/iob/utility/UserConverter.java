package iob.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import iob.data.UserEntity;
import iob.data.UserRole;
import iob.logic.exceptions.BadRequestException;
import iob.restAPI.UserBoundary;

@Component
public class UserConverter {

	private String configurableDomain;
	private String text;

	@Value("${spring.application.name:2022b}")
	public void setConfigurableDomain(String configurableDomain) {
		this.configurableDomain = configurableDomain;
	}
	
	public UserEntity updateEntityByBoundary(UserEntity userEntity, UserBoundary update) {
		if (update.getAvatar() != null && !update.getAvatar().isEmpty()) {
			userEntity.setAvatar(update.getAvatar());
		}
		
		if (update.getUsername() != null && !update.getUsername().isEmpty()) {
			userEntity.setUsername(update.getUsername());
		}
		
		if(update.getRole() != null) {
			try {
				UserRole.valueOf(update.getRole());
				userEntity.setUserRole(UserRole.valueOf(update.getRole()));
			} catch (Exception e) {
			}
		}
		
		return userEntity;
	}

	public UserEntity toEntity(UserBoundary boundary) {
		UserEntity entity = new UserEntity();

		// Make sure new users email addresses are indeed standard addresses
		// Regular Expression
		text = boundary.getUserId().getEmail();
		checkNull(text);
		checkValidEmail(text);
		entity.setUserId(configurableDomain + "_" + text);

		// Make sure that the role of users contains valid values
		text = boundary.getRole();
		checkRole(text);
		entity.setUserRole(UserRole.valueOf(text));

		// Make sure the user names are not null or empty,
		text = boundary.getUsername();
		checkNull(text);
		entity.setUsername(text);

		text = boundary.getAvatar();
		checkNull(text);
		entity.setAvatar(text);

		return entity;
	}

	private void checkNull(String text) {
		if (text == null || text.isEmpty())
			throw new BadRequestException();
	}

	private void checkValidEmail(String email) {
		String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
		// Compile regular expression to get the pattern
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		if (!matcher.matches())
			throw new BadRequestException();

	}

	private void checkRole(String role) {
		try {
			UserRole.valueOf(role);
		} catch (Exception e) {
			throw new BadRequestException();
		}
	}

	public UserBoundary toBoundary(UserEntity entity) {
		UserBoundary boundary = new UserBoundary();

		String[] splitedId = entity.getUserId().split("_");
		boundary.setUserId(new DomainWithEmail(splitedId[0], splitedId[1]));
		boundary.setAvatar(entity.getAvatar());
		boundary.setRole(entity.getUserRole().toString());
		boundary.setUsername(entity.getUsername());

		return boundary;
	}
}