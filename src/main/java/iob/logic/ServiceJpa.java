package iob.logic;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import iob.data.ActivityCrud;
import iob.data.InstanceCrud;
import iob.data.InstanceEntity;
import iob.data.UserCrud;
import iob.data.UserEntity;
import iob.data.UserRole;
import iob.utility.DomainWithEmail;
import iob.utility.DomainWithId;

public abstract class ServiceJpa {
	protected ActivityCrud activityCrud;
	protected UserCrud userCrud;
	protected InstanceCrud instanceCrud;
	protected String configurableDomain;
	
	@Autowired
	public ServiceJpa(UserCrud userCrud, ActivityCrud activityCrud, InstanceCrud instanceCrud) {
		this.userCrud = userCrud;
		this.activityCrud = activityCrud;
		this.instanceCrud = instanceCrud;
	}
	
	@Value("${spring.application.name:2022b}")
	protected void setConfigurableDomain(String configurableDomain) {
		this.configurableDomain = configurableDomain;
	}
	
	@Transactional(readOnly = true)
	protected UserRole getUserRoleInDB(String domain, String email) {
		String id = domain + "_" + email;

		Optional<UserEntity> op = userCrud.findById(id);
		if (op.isPresent())
			return op.get().getUserRole();
		return null;
	}
	
	@Transactional(readOnly = true)
	protected boolean checkUserIdInDB(DomainWithEmail domainWithEmail) {
		String id = domainWithEmail.getDomain() + "_" + domainWithEmail.getEmail();

		Optional<UserEntity> op = userCrud.findById(id);
		if (op.isPresent())
			return true;
		return false;
	}
	
	@Transactional(readOnly = true)
	protected boolean checkInstanceIdInDB(DomainWithId domainWithId) {
		String id = domainWithId.getDomain() + "_" + domainWithId.getId() ;
		
		Optional<InstanceEntity> op = instanceCrud.findById(id);
		if (op.isPresent())
			return true;
		return false;
	}
	
	@Transactional(readOnly = true)
	protected boolean getInstanceActiveInDB(DomainWithId domainWithId) {
		String id = domainWithId.getDomain() + "_" + domainWithId.getId() ;
		
		Optional<InstanceEntity> op = instanceCrud.findById(id);
		if (op.isPresent() && op.get().getActive())
			return true;
		return false;
	}
}
