package sg.com.ncs.brain.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sg.com.ncs.brain.services.RolesService;

@RestController
@RequestMapping("api")
public class RolesController {

	@Autowired
	private RolesService rolesService;

	@Autowired
	private AuditorAware auditAware;

	@ApiOperation("Fetch Models")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The Roles were successfully fetched"),
			@ApiResponse(code = 404, message = "The Roles were not successfully fetched"),
			@ApiResponse(code = 500, message = "The Roles were not successfully fetched") })
	@GetMapping(value = "/v1/roles", produces = "application/json;charset=UTF-8")
	public List<Map<String, Object>> fetch() {
		return rolesService.fetchRoles(auditAware.getCurrentAuditor().get().toString());
	}

}
