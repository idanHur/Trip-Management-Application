package iob.restAPI;

import java.util.Date;
import java.util.Map;

import iob.utility.CreatedBy;
import iob.utility.DomainWithId;
import iob.utility.Location;

public class InstanceBoundary {
	private DomainWithId instanceId;
	private String type;
	private String name;
	private Boolean active;
	private Date createdTimestamp;
	private CreatedBy createdBy;
	private Location location;
	private Map<String, Object> instanceAttributes;
	
	public InstanceBoundary() {
		super();
	}
	
	public InstanceBoundary(DomainWithId instanceId) {
		this.instanceId = instanceId;
	}

	public DomainWithId getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(DomainWithId instanceId) {
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

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public CreatedBy getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(CreatedBy createdBy) {
		this.createdBy = createdBy;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Map<String, Object> getInstanceAttributes() {
		return instanceAttributes;
	}

	public void setInstanceAttributes(Map<String, Object> instanceAttributes) {
		this.instanceAttributes = instanceAttributes;
	}

	@Override
	public String toString() {
		return "InstanceBoundary [instanceId=" + instanceId + ", type=" + type + ", name=" + name + ", active=" + active
				+ ", createdTimestamp=" + createdTimestamp + ", createdBy=" + createdBy + ", location=" + location
				+ ", instanceAttributes=" + instanceAttributes + "]";
	}
	
}
