package sg.com.ncs.brain.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sg.com.ncs.brain.entities.model.ModelDatasetSkeletonFields;
import sg.com.ncs.brain.model.dataset.Datasets;
import sg.com.ncs.brain.repository.DataSetSkeletonFieldsRepository;
import sg.com.ncs.brain.repository.DatasetRepository;
import sg.com.ncs.brain.services.DatasetService;
import sg.com.ncs.brain.utils.SuccessHandler;
import sg.com.ncs.common.exceptions.CustomException;
import sg.com.ncs.common.exceptions.NoDataFoundException;

@RestController
@RequestMapping("/api")
public class DataSetController {

	@Autowired
	private DatasetRepository datasetRepository;

	@Autowired
	private DatasetService datasetService;

	@Autowired
	private DataSetSkeletonFieldsRepository dataSetSkeletonFieldsRepository;

	@ApiOperation("Add Datasets")
	@PostMapping(value = "/v1/import_dataset")
	public ResponseEntity add(@RequestBody Map<?, ?> datasets) {
		Datasets datasetDb = datasetService.addDatasets(datasets);
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);

		Map<String, Object> innerMap = new HashMap<>();

		innerMap.put("message", "Successfully Saved");
		innerMap.put("createdDs_id", datasetDb.getId());
		response.put("message", innerMap);

		return ResponseEntity.status(HttpStatus.OK).body(response);

	}

	@ApiOperation("Fetch Dataset By model")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The Dataset were successfully fetched"),
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "The Dataset were not successfully fetched") })
	@GetMapping(value = "/v1/getdsformodel", produces = "application/json;charset=UTF-8")
	public ResponseEntity<?> fetch(@RequestHeader HttpHeaders headers) {

		if (!headers.containsKey("model_id")) {

			throw new CustomException("model id not specified");
		}
		Integer modelId = Integer.parseInt(headers.get("model_id").get(0));
		return ResponseEntity.status(HttpStatus.OK).body(datasetService.getDsForModel(modelId));
	}

	@ApiOperation("Fetch raw Dataset")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The Dataset were successfully fetched"),
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "The Dataset were not successfully fetched") })
	@GetMapping(value = "/v1/get_raw_ds_data_by_id", produces = "application/json;charset=UTF-8")
	public ResponseEntity<?> fetchDs(@RequestHeader HttpHeaders headers) {

		if (!headers.containsKey("dataset_id")) {

			throw new CustomException("dataset id not specified");
		}
		Integer datasetId = Integer.parseInt(headers.get("dataset_id").get(0));
		return ResponseEntity.status(HttpStatus.OK).body(datasetService.getRawDataById(datasetId));
	}

	@ApiOperation("Save Datasets")
	@PostMapping(value = "/v1/save_dataset")
	public ResponseEntity update(@RequestBody Map<?, ?> datasets) {
		Map<String, Object> res = new HashMap<>();
		Map<?, Object> dataset = datasetService.updateDatasets(datasets);
		res.put("message", dataset);
		return ResponseEntity.status(HttpStatus.OK).body(res);
	}

	@ApiOperation("Fetch List of Dataset")
	@GetMapping(value = "/v1/dataset/{id}")
	public ResponseEntity<Datasets> fetch(@PathVariable("id") Integer id) {

		Optional<Datasets> dataset = datasetRepository.findById(id);

		if (dataset.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(dataset.get());
		} else {

			throw new NoDataFoundException("no dataset found");
		}
	}

	@ApiOperation("Fetch List of Dataset")
	@GetMapping(value = "/v1/dataset")
	public List<Datasets> fetchDatasets() {
		return datasetRepository.findAll();
	}

	@ApiOperation("Import DataSet Skeleton")
	@PostMapping(value = "/api/v1/dataset-skeleton")
	public List<ModelDatasetSkeletonFields> importDataSetSkeleton(
			@Valid @RequestBody List<ModelDatasetSkeletonFields> modelDatasetSkeletonFields) {
		return dataSetSkeletonFieldsRepository.saveAll(modelDatasetSkeletonFields);
	}

	@ApiOperation("Fetch DataSet Skeleton")
	@GetMapping(value = "/api/v1/dataset-skeleton")
	public List<ModelDatasetSkeletonFields> fetchDataSetSkeleton() {
		return dataSetSkeletonFieldsRepository.findAll();
	}

	@ApiOperation("Fetch DataSet Skeleton By Id")
	@GetMapping(value = "/api/v1/dataset-skeleton/{id}")
	public ResponseEntity<ModelDatasetSkeletonFields> fetchDataSetSkeletonById(@PathVariable("id") Integer id) {

		Optional<ModelDatasetSkeletonFields> dataset = dataSetSkeletonFieldsRepository.findById(id);

		if (dataset.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(dataset.get());
		} else {

			throw new NoDataFoundException("no dataset found");
		}

	}

}
