package sg.com.ncs.brain.services;

import java.util.List;

import org.springframework.stereotype.Service;

import sg.com.ncs.brain.entities.user.maint.User;

@Service
public interface ImportUserService {

	List<User> addUser(List<User> users);

	List<User> fetchUser();

}
