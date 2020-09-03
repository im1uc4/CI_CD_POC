package sg.com.ncs.brain.services;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;

import sg.com.ncs.brain.entities.user.maint.Permissions;

public interface UserService {

	List<Map<?, ?>> saveUsers(@RequestBody List<Map<?, ?>> users);

	List<Permissions> fetchPermissions(Integer roleId);

}
