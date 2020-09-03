package sg.com.ncs.brain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import sg.com.ncs.brain.entities.user.maint.Groups;

@Repository
public interface GroupsRepository extends JpaRepository<Groups, Integer> {

	List<Groups> findByNameIgnoreCase(String name);

	Boolean existsByNameAndStatus(String username, Boolean status);

	Boolean existsByName(String username);

	Groups findBynameAndStatus(String username, boolean b);

}