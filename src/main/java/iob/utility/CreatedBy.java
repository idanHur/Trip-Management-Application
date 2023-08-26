package iob.utility;

public class CreatedBy {
	private DomainWithEmail userId;
	
	public CreatedBy() {}

	public CreatedBy(DomainWithEmail userId) {
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
		return "CreatedBy [userId=" + userId + "]";
	};
	
}
