package sg.com.ncs.brain.controller;

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
import sg.com.ncs.brain.services.JobsService;
import sg.com.ncs.brain.utils.SuccessHandler;
import sg.com.ncs.common.exceptions.CustomException;

@RestController
@RequestMapping("/api")
public class JobsController {

	@Autowired
	private JobsService jobsService;

	@ApiOperation("Fetch Jobs")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The Jobs were successfully fetched"),
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "The Jobs were not successfully fetched") })
	@GetMapping(value = "/v1/getjobsformodel", produces = "application/json;charset=UTF-8")
	public ResponseEntity<?> fetch(@RequestHeader HttpHeaders headers) {

		if (!headers.containsKey("model_id")) {

			throw new CustomException("model id not specified");
		}
		Integer modelId = Integer.parseInt(headers.get("model_id").get(0));
		return ResponseEntity.status(HttpStatus.OK).body(jobsService.getJobsForModel(modelId));
	}

	@ApiOperation("Fetch Jobs Status")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The Jobs Status were successfully fetched"),
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "The Jobs Status were not successfully fetched") })
	@GetMapping(value = "/v1/get_jobs_status", produces = "application/json;charset=UTF-8")
	public ResponseEntity<List<Map<String, List<Map<String, Object>>>>> fetchJobStatus(
			@RequestHeader HttpHeaders headers) {

		if (!headers.containsKey("jobid")) {

			throw new CustomException("jobid id not specified");
		}
		String jobId = headers.get("jobid").get(0).toString();

		return ResponseEntity.status(HttpStatus.OK).body(jobsService.getJobsStatus(jobId));
	}

	@ApiOperation("Add Jobs Call Back")
	@PostMapping(value = "/v1/jobscallback", produces = "application/json;charset=UTF-8")
	public ResponseEntity<Map<String, String>> insert(@RequestBody Map<?, ?> req) {
		ResponseEntity.status(HttpStatus.OK).body(jobsService.addJobsCallBack(req));
		return new SuccessHandler().success();
	}

}
