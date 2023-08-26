package iob.utility;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import iob.data.ActivityEntity;
import iob.logic.exceptions.BadRequestException;
import iob.restAPI.ActivityBoundary;

@Component
public class ActivityConvertor {

	private ObjectMapper jackson;

	@PostConstruct
	public void init() {
		this.jackson = new ObjectMapper();
	}

	private String getId(String domain, String id) {
		if (id == null || domain == null)
			return null;
		return domain + "_" + id;
	}

	public ActivityEntity toEntity(ActivityBoundary boundary) {
		ActivityEntity entity = new ActivityEntity();

		entity.setCreatedTimestamp(boundary.getCreatedTimestamp());
		entity.setActicityId(getId(boundary.getActivityId().getDomain(), boundary.getActivityId().getId()));

		String text = boundary.getType();
		checkNull(text);
		entity.setType(boundary.getType());

		if (boundary.getActivityAttributes() != null)
			entity.setActivityAttributes(toStringFromMap(boundary.getActivityAttributes()));

		entity.setInvokedBy(
				getId(boundary.getInvokedBy().getUserId().getDomain(), boundary.getInvokedBy().getUserId().getEmail()));
		
		entity.setInstance(getId(boundary.getInstance().getInstanceId().getDomain(),
				boundary.getInstance().getInstanceId().getId()));
		return entity;
	}

	private void checkNull(String text) {
		if (text == null || text.isEmpty())
			throw new BadRequestException();
	}

	public ActivityBoundary toBoundary(ActivityEntity entity) {
		ActivityBoundary boundary = new ActivityBoundary();

		String[] splitedId = entity.getActicityId().split("_");
		boundary.setActivityId(new DomainWithId(splitedId[0], splitedId[1]));

		if (entity.getActivityAttributes() != null)
			boundary.setActivityAttributes(toMapFromJsonString(entity.getActivityAttributes()));

		boundary.setCreatedTimestamp(entity.getCreatedTimestamp());
		String[] splitedInstanceId = entity.getInstance().split("_");
		boundary.setInstance(new Instance(new DomainWithId(splitedInstanceId[0], splitedInstanceId[1])));

		String[] splitedInvokedById = entity.getInvokedBy().split("_");
		boundary.setInvokedBy(new InvokedBy(new DomainWithEmail(splitedInvokedById[0], splitedInvokedById[1])));

		boundary.setType(entity.getType());

		return boundary;
	}

	private String toStringFromMap(Map<String, Object> object) {
		try {
			return this.jackson.writeValueAsString(object);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Map<String, Object> toMapFromJsonString(String json) {
		try {
			return this.jackson.readValue(json, Map.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
