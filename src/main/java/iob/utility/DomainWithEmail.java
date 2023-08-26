package iob.utility;

public class DomainWithEmail extends Domain{
	private String email;

	public DomainWithEmail() {

	}

	public DomainWithEmail(String domain, String email) {
		this.domain = domain;
		this.email = email;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "UserId [domain=" + domain + ", email=" + email + "]";
	}
}
