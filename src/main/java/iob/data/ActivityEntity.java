package iob.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/*
ACTIVITIES_TABLE
------------------------------------------------------------------------------------------------------------------------------
ACTIVITY_ID  | TYPE          | INSTANCE    | MESSAGE_TS    | INVOKE_BY      | FLEXIBLE_FIELD |
VARCHAR(255) | VARCHAR(255)  | VARCHAR(25) | 		   	   | VARCHAR(255)   |  CLOB          | 
<PK>		 |               |             |               |                |                |
 */
@Entity
@Table(name = "ACTIVITIES_TABLE")
public class ActivityEntity {

	private String acticityId;
	private String type;
	private String instance;
	private Date createdTimestamp;
	private String invokedBy;
	private String activityAttributes;

	public ActivityEntity() {
	}

	@Id
	public String getActicityId() {
		return acticityId;
	}

	public void setActicityId(String acticityId) {
		this.acticityId = acticityId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getInvokedBy() {
		return invokedBy;
	}

	public void setInvokedBy(String invokedBy) {
		this.invokedBy = invokedBy;
	}

	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ACTIVITY_TS")
	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	@Lob
	public String getActivityAttributes() {
		return activityAttributes;
	}

	public void setActivityAttributes(String activityAttributes) {
		this.activityAttributes = activityAttributes;
	}

}
