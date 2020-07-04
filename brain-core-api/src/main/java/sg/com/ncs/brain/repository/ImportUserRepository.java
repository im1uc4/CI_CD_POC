package sg.com.ncs.brain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.com.ncs.brain.entities.user.maint.User;



@Repository
public interface ImportUserRepository extends JpaRepository<User, Long> {

}
