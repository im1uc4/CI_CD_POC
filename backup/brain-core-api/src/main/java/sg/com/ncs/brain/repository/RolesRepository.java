package sg.com.ncs.brain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sg.com.ncs.brain.entities.user.maint.Roles;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer> {

	List<Roles> findByName(String name);

	/*
	 * @Query(value = "Select * from roles", nativeQuery = true) List<List<Object>>
	 * findRoles(@Param("userName") String userName);
	 */

	@Query(value = "SELECT \r\n" + "    roles.id,\r\n" + "    roles.name,\r\n" + "    'NULL' AS permissions,\r\n"
			+ "    TRUE AS iSCoreRole\r\n" + "FROM\r\n" + "    user_roles\r\n" + "        INNER JOIN\r\n"
			+ "    users ON user_roles.usersId = users.id\r\n" + "        INNER JOIN\r\n"
			+ "    roles ON user_roles.roleId = roles.id\r\n" + "WHERE\r\n" + "    users.username = :userName\r\n"
			+ "ORDER BY power", nativeQuery = true)
	List<Object[]> findRoles(@Param("userName") String userName);

	Boolean existsByName(String name);

}