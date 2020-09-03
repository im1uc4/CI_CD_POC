package sg.com.ncs.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.com.ncs.auth.user.maint.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	public List<User> findByUsername(String userName);

}