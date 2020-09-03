package sg.com.ncs.brain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sg.com.ncs.brain.entities.model.UserModelAccess;

@Repository
public interface UserModelAccessRepository extends JpaRepository<UserModelAccess, Integer> {

	List<UserModelAccess> findAllByUsername(String username);

	@Query(value = " select\r\n" + 
			"        id ,\r\n" + 
			"        access_type,\r\n" + 
			"        created_by,\r\n" + 
			"        model_description_details ,\r\n" + 
			"        model_description_header,\r\n" + 
			"        model_name,\r\n" + 
			"        person_in_charge ,\r\n" + 
			"        username\r\n" + 
			"    from\r\n" + 
			"        user_model_access\r\n" + 
			"    where\r\n" + 
			"        username=:userName", nativeQuery = true)
	List<Object[]> findUsingUsername(@Param("userName") String userName);
}
