package sg.com.ncs.brain.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import sg.com.ncs.brain.entities.model.Constraints;
import sg.com.ncs.brain.services.ConstraintsService;
import sg.com.ncs.brain.utils.SuccessHandler;
import sg.com.ncs.common.exceptions.CustomException;

@RestController
@RequestMapping("/api")
public class ConstraintsController {

	@Autowired
	ConstraintsService constraintsService;

	@ApiOperation("Fetch Constraints")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The Constraints were successfully fetched"),
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "The Constraints were not successfully fetched") })
	@GetMapping(value = "/v1/getconstraints", produces = "application/json;charset=UTF-8")
	public ResponseEntity fetch(/* @RequestBody String body, */ @RequestHeader HttpHeaders headers) {

		if (!headers.containsKey("model_id")) {

			throw new CustomException("model id not specified");
		}

		Integer modelId = Integer.parseInt(headers.get("model_id").get(0));

		Map<String, List<Map<String, Object>>> constraints = constraintsService.fetchConstraints(modelId);

		if (constraints == null)
			throw new CustomException("Constraints not found for specified Model id.");

		return ResponseEntity.status(HttpStatus.OK).body(constraints);
	}

	/*
	 * @ApiOperation("Fetch All Constraints")
	 * 
	 * @ApiResponses(value = { @ApiResponse(code = 200, message =
	 * "The Constraints were successfully fetched"),
	 * 
	 * @ApiResponse(code = 200, message = "OK"),
	 * 
	 * @ApiResponse(code = 404, message =
	 * "The Constraints were not successfully fetched") })
	 * 
	 * @GetMapping(value = "/v1/constraints") public List<Constraints> fetchAll() {
	 * 
	 * return constraintsService.fetchAllConstraints(); }
	 */
	@ApiOperation("Add List of Contraint Types")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The Constraints were successfully Added"),
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "The Constraints were not successfully Added") })
	@PostMapping(value = "/v1/import_constraint_types")

	public ResponseEntity insert(@RequestBody Map<?, ?> req) {
		ResponseEntity.status(HttpStatus.OK).body(constraintsService.saveConstraints(req));

		Map<String, Object> response = new HashMap<String, Object>();
		response.put("success", true);
		response.put("message", "Constraint Types Were Imported");

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
