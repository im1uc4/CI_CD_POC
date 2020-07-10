package sg.com.ncs.brain.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import sg.com.ncs.brain.entities.user.maint.User;
import sg.com.ncs.brain.repository.ImportUserRepository;
import sg.com.ncs.brain.services.ImportUserService;

public class ImportUserServiceImpl implements ImportUserService {

	@Autowired
	ImportUserRepository repository;
	
	public List<User> addUser(List<User> users){
		
		return repository.saveAll(users);
	}

	public List<User> fetchUser() {
		return repository.findAll();

	}
	

}
