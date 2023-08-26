package iob.data;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserCrud extends PagingAndSortingRepository<UserEntity, String>{

}
