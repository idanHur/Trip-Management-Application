package iob.data;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface InstanceCrud extends PagingAndSortingRepository<InstanceEntity, String>{
	
	public List<InstanceEntity> findAllByActive (
			@Param("isActive") boolean isActive,
			Pageable pageable);
	
	public List<InstanceEntity> findAllByName (
			@Param("name") String name,
			Pageable pageable);
	
	public List<InstanceEntity> findAllByNameAndActive (
			@Param("name") String name,
			@Param("isActive") boolean isActive,
			Pageable pageable);
	
	public List<InstanceEntity> findAllByType (
			@Param("type") String type,
			Pageable pageable);
	
	public List<InstanceEntity> findAllByTypeAndActive (
			@Param("type") String type,
			@Param("isActive") boolean isActive,
			Pageable pageable);
	
	public List<InstanceEntity> findAllByLocationLatBetweenAndLocationLngBetween(
			@Param("minLocationLat") Double minLocationLat, 
			@Param("maxLocationLat") Double maxLocationLat, 
			@Param("minLocationLng") Double minLocationLng, 
			@Param("maxLocationLng") Double maxLocationLng,
            Pageable pageable);
	
	public List<InstanceEntity> findAllByLocationLatBetweenAndLocationLngBetweenAndActive(
			@Param("minLocationLat") Double minLocationLat, 
			@Param("maxLocationLat") Double maxLocationLat, 
			@Param("minLocationLng") Double minLocationLng, 
			@Param("maxLocationLng") Double maxLocationLng,
			@Param("isActive") boolean isActive,
            Pageable pageable);
}
