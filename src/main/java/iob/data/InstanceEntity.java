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
INSTANCE_TABLE
------------------------------------------------------------------------------------------------------------------------------
INSTANCE_ID		 | TYPE          | NAME 		 | ACTIVE	 | CREATED_TIME_STAMP | CREATED_BY   | LOCATION     | INSTANCE_ATTRIBUTES      
VARCHAR(255)     | VARCHAR(255)  |VARCHAR(255)   | boolean   | 					  | VARCHAR(255) | VARCHAR(255) | CLOB 
<PK>		     |               |            	 |           |         			  |              |              |              
 */

@Entity
@Table(name = "INSTANCE_TABLE")
public class InstanceEntity {

	private String instanceId;
	private String type;
	private String name;
	private Boolean active;
	private Date createdTimestamp;
	private String createdBy;
	private Double LocationLat;
	private Double LocationLng;
	private String instanceAttributes;

	@Id
	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INSTANCE_TS")
	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Double getLocationLat() {
		return LocationLat;
	}

	public void setLocationLat(Double locationLat) {
		LocationLat = locationLat;
	}

	public Double getLocationLng() {
		return LocationLng;
	}

	public void setLocationLng(Double locationLng) {
		LocationLng = locationLng;
	}

	@Lob
	public String getInstanceAttributes() {
		return instanceAttributes;
	}

	public void setInstanceAttributes(String instanceAttributes) {
		this.instanceAttributes = instanceAttributes;
	}

}