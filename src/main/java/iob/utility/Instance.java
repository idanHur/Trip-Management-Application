package iob.utility;

public class Instance {

	private DomainWithId instanceId;
	
	public Instance() {
		super();
	}

	public Instance(DomainWithId instanceId) {
		super();
		this.instanceId = instanceId;
	}

	public DomainWithId getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(DomainWithId instanceId) {
		this.instanceId = instanceId;
	}

	@Override
	public String toString() {
		return "Instance [instanceId=" + instanceId + "]";
	}
	
}
