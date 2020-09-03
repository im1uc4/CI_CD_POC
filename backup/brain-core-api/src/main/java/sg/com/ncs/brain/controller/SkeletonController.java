package sg.com.ncs.brain.controller;

import java.util.HashMap;
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
import sg.com.ncs.brain.services.SkeletonService;
import sg.com.ncs.brain.utils.SuccessHandler;
import sg.com.ncs.common.exceptions.CustomException;

@RestController
@RequestMapping("/api")
public class SkeletonController {

	@Autowired
	private SkeletonService skeletonService;

	@ApiOperation("insert skeleton")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The Skeleton were successfully inserted"),
			@ApiResponse(code = 500, message = "The Skeleton were not successfully Saved"),
			@ApiResponse(code = 404, message = "The Skeleton were not successfully inserted") })
	@PostMapping(value = "/v1/import_skeleton")
	public ResponseEntity insert(@RequestBody Map<?, ?> req) {

		ResponseEntity.status(HttpStatus.OK).body(skeletonService.addSkeleton(req));

		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "Successfully Saved");

		return ResponseEntity.status(HttpStatus.OK).body(response);

	}

	@ApiOperation("Fetch Dataset Skeleton")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The dataset skeleton were successfully fetched"),
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "The dataset skeleton were not successfully fetched") })
	@GetMapping(value = "/v1/get_ds_skeleton_columns_by_ds_id", produces = "application/json;charset=UTF-8")
	public ResponseEntity<?> fetch(@RequestHeader HttpHeaders headers) {

		if (!headers.containsKey("dataset_id")) {
			throw new CustomException("dataset id not specified");
		}
		Integer datasetId = Integer.parseInt(headers.get("dataset_id").get(0));
		return ResponseEntity.status(HttpStatus.OK).body(skeletonService.getSkeletonByDsId(datasetId));
	}

}
