package sg.com.ncs.brain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.com.ncs.brain.entities.user.maint.Permissions;

@Repository
public interface PermissionsRepository extends JpaRepository<Permissions, Integer> {

	List<Permissions> findAllByRoleId(Integer roleId);

}