package sg.com.ncs.brain.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sg.com.ncs.brain.entities.model.Model;
import sg.com.ncs.brain.entities.model.ModelAccess;
import sg.com.ncs.brain.services.ModelsService;
import sg.com.ncs.brain.utils.SuccessHandler;
import sg.com.ncs.common.exceptions.CustomException;

@RestController
@RequestMapping("/api")
public class ModelController {

	@Autowired
	private ModelsService modelsService;

	@ApiOperation("Fetch Models")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The Models were successfully fetched"),
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "The Models were not successfully fetched") })
	@GetMapping(value = "/v1/getmodels", produces = "application/json;charset=UTF-8")
	public ResponseEntity fetch() {
		return ResponseEntity.status(HttpStatus.OK).body(modelsService.fetchModels());
	}

	@ApiOperation("Add a set of Models")
	@PostMapping(value = "/v1/import_model")
	public ResponseEntity insert(@RequestBody Map<?, ?> modelBody) {
		if (!modelBody.containsKey("optimisationModel"))
			throw new CustomException("Model Body missing.");
		else {
			Map<?, ?> req = (Map<?, ?>) modelBody.get("optimisationModel");
			Model model = modelsService.addModel(req);

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);

			Map<String, Object> innerMap = new HashMap<>();

			innerMap.put("message", "Successfully Saved");
			innerMap.put("created_model", model.getId());
			response.put("message", innerMap);

			return ResponseEntity.status(HttpStatus.OK).body(response);

		}
	}

	@ApiOperation("Set model access")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The Group/User access for model saved successfully"),
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "The Group/User access for model not saved successfully") })
	@PostMapping(value = "/v1/set_model_access")
	public ResponseEntity saveModelAccess(@RequestBody Map<?, ?> modelAccessReq) {

		Map<?, ?> req = null;
		if (!modelAccessReq.containsKey("set_model_access"))
			throw new CustomException("set_model_access key is missing in the JSON body.");

		req = (Map<?, ?>) modelAccessReq.get("set_model_access");

		ModelAccess modelAccess = modelsService.saveModelAccess(req);

		if (modelAccess == null)
			throw new CustomException("Issue in import, please check with tech team");

		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "Successfully Saved");

		return ResponseEntity.status(HttpStatus.OK).body(response);

	}

	@ApiOperation("Change Model Status")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The model status changed successfully"),
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "The model status did not changed successfully") })
	@PostMapping(value = "/v1/change_model_status")
	public ResponseEntity<Map<String, String>> updateModelStatus(@RequestBody Map<?, ?> modelStatus) {

		if (!modelStatus.containsKey("optimisationModel"))
			throw new CustomException("Model is missing in the Import data");

		Map<?, ?> req = (Map<?, ?>) modelStatus.get("optimisationModel");

		modelsService.updateModelStatus(req);
		return new SuccessHandler().success();
	}
}