package sg.com.ncs.brain.services.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import sg.com.ncs.brain.entities.user.maint.Groups;
import sg.com.ncs.brain.entities.user.maint.Permissions;
import sg.com.ncs.brain.entities.user.maint.Roles;
import sg.com.ncs.brain.entities.user.maint.User;
import sg.com.ncs.brain.entities.user.maint.UserGroups;
import sg.com.ncs.brain.entities.user.maint.UserRole;
import sg.com.ncs.brain.repository.GroupsRepository;
import sg.com.ncs.brain.repository.PermissionsRepository;
import sg.com.ncs.brain.repository.RolesRepository;
import sg.com.ncs.brain.repository.UserGroupsRepository;
import sg.com.ncs.brain.repository.UserRepository;
import sg.com.ncs.brain.repository.UserRolesRepository;
import sg.com.ncs.brain.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PermissionsRepository permissionsRepository;

	@Autowired
	UserRolesRepository userRolesRepository;

	@Autowired
	UserGroupsRepository userGroupsRepository;

	@Autowired
	RolesRepository rolesRepository;

	@Autowired
	GroupsRepository groupsRepository;

	@Bean
	PasswordEncoder getEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public List<Map<?, ?>> saveUsers(List<Map<?, ?>> usersBody) {

		usersBody.forEach(user -> {

			User userEntity = new User();
			userEntity.setDutyTitle(user.get("duty title").toString());
			userEntity.setEmail(user.get("email").toString());
			userEntity.setFullname(user.get("fullname").toString());
			userEntity.setName(user.get("name").toString());
			userEntity.setPassword(getEncoder().encode(user.get("password").toString()));
			userEntity.setUsername(user.get("username").toString());
			userEntity.setStatus(true);

			User savedUser = userRepository.save(userEntity);

			List<String> roles = (List<String>) user.get("user role");

			roles.forEach(roleName -> {

				List<Roles> role = rolesRepository.findByName(roleName);
				UserRole userRoles = new UserRole();
				userRoles.setRoles(role.get(0));
				userRoles.setUser(savedUser);
				userRolesRepository.save(userRoles);

			});

			List<Groups> group = groupsRepository.findByNameIgnoreCase(user.get("group").toString());
			UserGroups userGroups = new UserGroups();
			userGroups.setGroup(group.get(0));
			userGroups.setUser(savedUser);
			userGroupsRepository.save(userGroups);

		});

		return usersBody;
	}

	@Override
	public List<Permissions> fetchPermissions(Integer roleId) {
		return permissionsRepository.findAllByRoleId(roleId);

	}

}
