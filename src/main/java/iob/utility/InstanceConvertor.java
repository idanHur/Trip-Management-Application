package iob.utility;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import iob.data.InstanceEntity;
import iob.logic.exceptions.BadRequestException;
import iob.restAPI.InstanceBoundary;

@Component
public class InstanceConvertor {

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
	
	public InstanceEntity updateEntityByBoundary(InstanceEntity entity, InstanceBoundary update) {
		if (update.getActive() != null)
			entity.setActive(update.getActive());
		
		if (update.getInstanceAttributes() != null)
			entity.setInstanceAttributes(toStringFromMap(update.getInstanceAttributes()));
		
		if (update.getLocation() != null) {
			if (update.getLocation().getLat() != null)
				entity.setLocationLat(update.getLocation().getLat());
			if (update.getLocation().getLng() != null)
				entity.setLocationLng(update.getLocation().getLng());
		}
		
		if (update.getName() != null && !update.getName().isEmpty())
			entity.setName(update.getName());
		
		if (update.getType() != null && !update.getType().isEmpty())
			entity.setType(update.getType());
		
		return entity;
	}

	public InstanceEntity toEntity(InstanceBoundary boundary) {
		InstanceEntity entity = new InstanceEntity();

		entity.setActive(boundary.getActive());

		entity.setCreatedBy(
				getId(boundary.getCreatedBy().getUserId().getDomain(),
						boundary.getCreatedBy().getUserId().getEmail()));

		entity.setCreatedTimestamp(boundary.getCreatedTimestamp());

		if (boundary.getInstanceAttributes() != null)
			entity.setInstanceAttributes(toStringFromMap(boundary.getInstanceAttributes()));

		entity.setLocationLat(boundary.getLocation().getLat());

		entity.setLocationLng(boundary.getLocation().getLng());

		checkNull(boundary.getName());
		entity.setName(boundary.getName());

		checkNull(boundary.getType());
		entity.setType(boundary.getType());

		entity.setInstanceId(getId(boundary.getInstanceId().getDomain(), boundary.getInstanceId().getId()));

		return entity;
	}

	public InstanceBoundary toBoundary(InstanceEntity entity) {
		InstanceBoundary boundary = new InstanceBoundary();

		boundary.setActive(entity.getActive());

		String[] splitedId = entity.getCreatedBy().split("_");
		boundary.setCreatedBy(new CreatedBy(new DomainWithEmail(splitedId[0], splitedId[1])));

		boundary.setCreatedTimestamp(entity.getCreatedTimestamp());

		boundary.setLocation(new Location(entity.getLocationLat(), entity.getLocationLng()));

		boundary.setName(entity.getName());

		boundary.setType(entity.getType());

		if (entity.getInstanceAttributes() != null)
			boundary.setInstanceAttributes(toMapFromJsonString(entity.getInstanceAttributes()));

		String[] splitedInstanceId = entity.getInstanceId().split("_");
		boundary.setInstanceId(new DomainWithId(splitedInstanceId[0], splitedInstanceId[1]));

		return boundary;
	}

	private void checkNull(String text) {
		if (text == null || text.isEmpty())
			throw new BadRequestException();
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
