package sg.com.ncs.brain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.com.ncs.brain.entities.user.maint.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	User findByUsername(String username);

	Boolean existsByUsername(String grantForUser);

	Boolean existsByUsernameAndStatus(String grantForUser, Boolean status);

	User findByUsernameAndStatus(String username, boolean b);

}