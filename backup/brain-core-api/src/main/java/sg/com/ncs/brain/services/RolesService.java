package sg.com.ncs.brain.services;

import java.util.List;
import java.util.Map;

public interface RolesService {

	List<Map<String, Object>> fetchRoles(String userName);

}
