package sg.com.ncs.brain.seeders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import sg.com.ncs.brain.controller.UserController;
import sg.com.ncs.brain.entities.user.maint.Groups;
import sg.com.ncs.brain.entities.user.maint.Roles;
import sg.com.ncs.brain.repository.GroupsRepository;
import sg.com.ncs.brain.repository.RolesRepository;
import sg.com.ncs.brain.repository.UserRepository;

@Component
public class DatabaseSeeder {

	Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);

	@Autowired
	UserController userController;

	@Autowired
	UserRepository userRepository;

	@Autowired
	GroupsRepository groupsRepository;


	@Autowired
	RolesRepository rolesRepository;

	@EventListener
	public void seed(ContextRefreshedEvent event) {
		seedRolesTable();
		seedGroupsTable();
		seedAdminUser();
	}

	private void seedRolesTable() {

		Boolean roleExists = rolesRepository.existsByName("System Administrator");
		Roles initialRoles = new Roles();

		if (!roleExists) {
			initialRoles.setName("System Administrator");
			initialRoles.setPower(3);
			initialRoles.setStatus(true);
			rolesRepository.save(initialRoles);
		}

		initialRoles = new Roles();
		roleExists = rolesRepository.existsByName("Module Administrator");

		if (!roleExists) {
			initialRoles.setName("Module Administrator");
			initialRoles.setPower(2);
			initialRoles.setStatus(true);
			rolesRepository.save(initialRoles);
		}

		initialRoles = new Roles();
		roleExists = rolesRepository.existsByName("Module User");

		if (!roleExists) {
			initialRoles.setName("Module User");
			initialRoles.setPower(1);
			initialRoles.setStatus(true);
			rolesRepository.save(initialRoles);
		}
	}

	private void seedGroupsTable() {

		Boolean groupExists = groupsRepository.existsByName("General");
		if (!groupExists) {
			Groups initialGroups = new Groups();
			initialGroups.setName("General");
			initialGroups.setStatus(true);
			groupsRepository.save(initialGroups);
		}
	}

	
	private void seedAdminUser() {

		Map<String, Object> user = new HashMap<>();
		List<Map<?, ?>> userList = new ArrayList();

		user.put("duty title", "Duty Title");
		user.put("email", "email@email.com");
		user.put("fullname", "Administrator");
		user.put("group", "general");
		user.put("name", "Admin");
		user.put("password", "P@ssw0rd123456");
		user.put("username", "admin");
		List userRole = new ArrayList<>();
		userRole.add("Module User");
		user.put("user role", userRole);

		userList.add(user);

		Map<String, List<Map<?, ?>>> userBody = new HashMap<>();

		userBody.put("userlist", userList);

		Boolean userExists = userRepository.existsByUsername("admin");

		if (!userExists)
			userController.save(userBody);

	}

}
