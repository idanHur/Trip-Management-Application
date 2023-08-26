package iob.data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

/*
USERS_TABLE
---------------------------
//USER_ID      | USER_ROLE    | USER_NAME    | AVATAR       |
//VARCHAR(255) | VARCHAR(255) | VARCHAR(255) | VARCHAR(255) |
<PK>		 
 */
@Entity
@Table(name="USERS")
public class UserEntity  {
	
	private String userId;
	private UserRole userRole;
	private String userName;
	private String avatar;

	public UserEntity() {
	}

	@Id
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Enumerated(EnumType.STRING)
	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public String getUsername() {
		return userName;
	}

	public void setUsername(String username) {
		this.userName = username;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

}
