package iob.utility;

public class DomainWithId extends Domain{
	
	private String id;
	
	public DomainWithId() {
		
	}
	
	public DomainWithId(String domain, String id) {
		super();
		this.domain = domain;
		this.id = id;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "ActicityId [domain=" + domain + ", id=" + id + "]";
	}
}
