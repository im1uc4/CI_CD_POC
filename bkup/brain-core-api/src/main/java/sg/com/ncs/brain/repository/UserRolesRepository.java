package sg.com.ncs.brain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.com.ncs.brain.entities.user.maint.UserRole;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRole, Integer> {
}