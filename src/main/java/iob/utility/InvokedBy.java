package iob.utility;

public class InvokedBy {

	private DomainWithEmail userId;
	
	public InvokedBy() {
		super();
	}

	public InvokedBy(DomainWithEmail userId) {
		super();
		this.userId = userId;
	}

	public DomainWithEmail getUserId() {
		return userId;
	}

	public void setUserId(DomainWithEmail userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "InvokedBy [userId=" + userId + "]";
	}
	
}
