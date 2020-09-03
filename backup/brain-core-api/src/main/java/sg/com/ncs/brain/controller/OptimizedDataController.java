package sg.com.ncs.brain.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sg.com.ncs.brain.services.JobsService;
import sg.com.ncs.common.exceptions.CustomException;

@RestController
@RequestMapping("/api")
public class OptimizedDataController {

	@Autowired
	private JobsService jobsService;

	@ApiOperation("Fetch Optimized Jobs")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The Optimized Jobs were successfully fetched"),
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "The Optimized jobs were not successfully fetched") })
	@GetMapping(value = "/v1/get_optimized_data_for_model", produces = "application/json;charset=UTF-8")
	public List<Map<String, List<Map<String, Object>>>> get(
			/* @RequestBody String body, */ @RequestHeader HttpHeaders headers) {

		if (!headers.containsKey("jobId")) {

			throw new CustomException("job id not specified");
		}

		String jobId = headers.get("jobId").get(0).toString();

		List<Map<String, List<Map<String, Object>>>> jobs = jobsService.fetchOptimizedJobs(jobId);

		if (jobs == null || !(jobs.size() > 0))
			throw new CustomException("Jobs not found for specified job id.");

		return jobs;

	}

	@ApiOperation("Update Optimized Data")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The Optimized Jobs were successfully updated"),
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "The Optimized jobs were not successfully updated") })
	@PutMapping(value = "/v1/update_optimized_data_for_model")
	public ResponseEntity<List<Integer>> update(@RequestBody Map<?, ?> req) {

		return ResponseEntity.status(HttpStatus.OK).body(jobsService.updateOptimizedJobs(req));
	}

	@ApiOperation("Delete Optimized Data")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The Optimized Jobs were successfully deleted"),
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "The Optimized jobs were not successfully deleted") })
	@DeleteMapping(value = "/v1/delete_optimized_data_for_model")
	public ResponseEntity<List<Integer>> delete(@RequestBody Map<?, ?> req) {

		return ResponseEntity.status(HttpStatus.OK).body(jobsService.deleteOptimizedJobs(req));
	}

}
