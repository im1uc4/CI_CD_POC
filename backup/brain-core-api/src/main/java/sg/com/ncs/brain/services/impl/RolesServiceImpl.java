package sg.com.ncs.brain.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.com.ncs.brain.repository.RolesRepository;
import sg.com.ncs.brain.services.RolesService;

@Service
public class RolesServiceImpl implements RolesService {

	@Autowired
	RolesRepository rolesRepository;

	@Override
	public List<Map<String, Object>> fetchRoles(String userName) {
		// TODO Auto-generated method stub

		List<Object[]> roles = rolesRepository.findRoles(userName);

		List<Map<String, Object>> res = new ArrayList<>();

		roles.forEach(role -> {
			Map<String, Object> roleMap = new HashMap<>();

			roleMap.put("isCoreRole", role[3]);
			roleMap.put("permissions", role[2]);
			roleMap.put("name", role[1]);
			roleMap.put("id", role[0]);

			res.add(roleMap);

		});
		return res;
	}
}
