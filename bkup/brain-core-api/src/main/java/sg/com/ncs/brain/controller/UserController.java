package sg.com.ncs.brain.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sg.com.ncs.brain.entities.user.maint.Permissions;
import sg.com.ncs.brain.entities.user.pojo.UsersBody;
import sg.com.ncs.brain.services.UserService;
import sg.com.ncs.brain.utils.SuccessHandler;
import sg.com.ncs.common.exceptions.CustomException;
import sg.com.ncs.common.exceptions.DataException;

@RestController
@RequestMapping("api")
public class UserController {

	@Autowired
	private UserService userService;

	@SuppressWarnings("rawtypes")
	@ApiOperation("Add List of Users")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The Users were successfully Saved"),
			@ApiResponse(code = 500, message = "The Users were not successfully Saved"),
			@ApiResponse(code = 404, message = "The Users were not successfully Saved") })
	@PostMapping(value = "/v1/import_user")
	public ResponseEntity save(@Valid @RequestBody Map<String, List<Map<?, ?>>> usersBody) {

		if (!usersBody.containsKey("userlist"))
			throw new CustomException("userlist is missing");

		List<Map<?, ?>> users = usersBody.get("userlist");

		try {
			userService.saveUsers(users);
		} catch (Exception e) {
			throw new DataException("Error saving users");
		}
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "The Users were successfully Saved");

		return ResponseEntity.status(HttpStatus.OK).body(response);

	}

	@ApiOperation("Fetch Permissions")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The Permissions were successfully fetched"),
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "The Permissions were not successfully fetched") })
	@GetMapping(value = "/v1/get_permissions", produces = "application/json;charset=UTF-8")
	public Map<String, List<Permissions>> fetch(/* @RequestBody String body, */ @RequestHeader HttpHeaders headers) {

		Map<String, List<Permissions>> permissionsMap = new HashMap<>();

		if (!headers.containsKey("role")) {

			throw new CustomException("role id not specified");
		}

		Integer roleId = Integer.parseInt(headers.get("role").get(0));

		List<Permissions> permissions = userService.fetchPermissions(roleId);

		if (permissions == null || !(permissions.size() > 0))
			throw new CustomException("Permissions not found for specified Role.");

		permissionsMap.put("permissions", permissions);

		return permissionsMap;
	}

}
