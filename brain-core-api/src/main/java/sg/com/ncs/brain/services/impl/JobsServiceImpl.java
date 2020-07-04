package sg.com.ncs.brain.services.impl;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.com.ncs.brain.entities.model.Jobs;
import sg.com.ncs.brain.entities.model.JobsStatusUpdate;
import sg.com.ncs.brain.entities.model.Model;
import sg.com.ncs.brain.entities.model.ModelDatasetSkeletonTables;
import sg.com.ncs.brain.entities.model.OptimizedJobs;
import sg.com.ncs.brain.model.dataset.Datasets;
import sg.com.ncs.brain.repository.DatasetDataRepository;
import sg.com.ncs.brain.repository.DatasetRepository;
import sg.com.ncs.brain.repository.JobsRepository;
import sg.com.ncs.brain.repository.JobsStatusRepository;
import sg.com.ncs.brain.repository.ModelSkeletonTablesRepository;
import sg.com.ncs.brain.repository.OptimizedJobsRepository;
import sg.com.ncs.brain.services.JobsService;
import sg.com.ncs.common.exceptions.CustomException;

@Service
public class JobsServiceImpl implements JobsService {

	@Autowired
	JobsRepository jobsRepository;

	@Autowired
	DatasetServiceImpl datasetServiceImpl;

	@Autowired
	JobsStatusRepository jobsStatusRepository;

	@Autowired
	OptimizedJobsRepository optimizedJobsRepository;

	@Autowired
	DatasetRepository datasetRepository;

	@Autowired
	DatasetDataRepository datasetDataRepository;

	@Autowired
	ModelSkeletonTablesRepository modelSkeletonTablesRepository;

	public List<Jobs> fetchJobs(Long id) {
		return jobsRepository.findAllByModelId(id);

	}

	public List<JobsStatusUpdate> fetchJobStatus(Long id) {
		return jobsRepository.findAllByJobId(id);

	}

	@Override
	public Object addJobsCallBack(Map<?, ?> req) {

		if (!req.containsKey("jobStatusCode"))
			throw new CustomException("job status is missing");

		if (!req.get("jobStatusCode").toString().trim().equals("COMPLETED"))
			throw new CustomException("To import callback, job status has to be 'COMPLETED'( Currently is "
					+ req.get("jobStatusCode").toString());

		if (!req.containsKey("job"))
			throw new CustomException("job segment is missing");

		if (!req.containsKey("bestSolution"))
			throw new CustomException("bestSolution segment is missing");

		saveJsonCallBack((Map<?, ?>) req.get("bestSolution"), (Map<?, ?>) req.get("job"),
				req.get("jobStatusCode").toString().trim());

		return null;
	}

	private void saveJsonCallBack(Map<?, ?> jsonObjBs, Map<?, ?> jsonObjJob, String status) {

		String jobId = jsonObjJob.containsKey("jobId") ? jsonObjJob.get("jobId").toString() : null;
		String completedDateTime = jsonObjJob.containsKey("completedDateTime")
				? jsonObjJob.get("completedDateTime").toString()
				: null;

//		Map<?, ?> returnObject = new HashMap<>();

		Jobs job = updateJobStatus(jobId, status, completedDateTime);

		insertOptimizedData(jsonObjBs, job);

	}

	private Boolean insertOptimizedData(Map<?, ?> jsonObjBs, Jobs job) {

		String datasetId = jsonObjBs.containsKey("datasetId") ? jsonObjBs.get("datasetId").toString() : null;

		Datasets dsIdObj = retrieveDataSet(datasetId);

		List<Map<String, Object>> datasetDataObj = new ArrayList<>();

		List<ModelDatasetSkeletonTables> tableStruct = getDsTableStructure(dsIdObj.getModel());

		ModelDatasetSkeletonTables resourceTable = tableStruct.stream()
				.filter(table -> table.getTableName().equals("resources")).findFirst().orElse(null);
		ModelDatasetSkeletonTables servicesTable = tableStruct.stream()
				.filter(table -> table.getTableName().equals("services")).findFirst().orElse(null);

		if (resourceTable != null && servicesTable != null) {
			datasetDataObj = retrieveDataSetData(dsIdObj.getId(), resourceTable.getId(), servicesTable.getId());
		}

		insertTaskList((List<Map<?, ?>>) jsonObjBs.get("taskList"), job, datasetDataObj);

		return true;
	}

	private void insertTaskList(List<Map<?, ?>> tasksList, Jobs job, List<Map<String, Object>> datasetDataObj) {

		List<OptimizedJobs> optimizedJobsDb = optimizedJobsRepository.findByJobId(job.getId());

		optimizedJobsDb.forEach(optimizedJobDb -> {
			optimizedJobsRepository.delete(optimizedJobDb);
		});

		List<OptimizedJobs> optimizedJobsList = new ArrayList<>();

		for (Map<?, ?> task : tasksList) {
			String taskId = null;
			String taskName = null;
			String startDateTime = null;
			String endDateTime = null;
			String taskDuration = null;
			String resourceLocked = null;
			String customFieldList = null;
			String startDateTimeLocked = null;
			String serviceId = null;
			String timeWindowId = null;
			String resourceId = null;
			String location = null;

			if (task.containsKey("taskId"))
				if (task.get("taskId") != null)
					taskId = task.get("taskId").toString();

			if (task.containsKey("taskName"))
				if (task.get("taskName") != null)
					taskName = task.get("taskName").toString();

			if (task.containsKey("startDateTime"))
				if (task.get("startDateTime") != null)
					startDateTime = task.get("startDateTime").toString();

			if (task.containsKey("endDateTime"))
				if (task.get("endDateTime") != null)
					endDateTime = task.get("endDateTime").toString();

			if (task.containsKey("taskDuration"))
				if (task.get("taskDuration") != null)
					taskDuration = task.get("taskDuration").toString();

			if (task.containsKey("resourceLocked"))
				if (task.get("resourceLocked") != null)
					resourceLocked = task.get("resourceLocked").toString();

			if (task.containsKey("customFieldList"))
				if (task.get("customFieldList") != null)
					customFieldList = task.get("customFieldList").toString();

			if (task.containsKey("startDateTimeLocked"))
				if (task.get("startDateTimeLocked") != null)
					startDateTimeLocked = task.get("startDateTimeLocked").toString();

			if (task.containsKey("serviceId"))
				if (task.get("serviceId") != null)
					serviceId = task.get("serviceId").toString();

			if (task.containsKey("timeWindowId"))
				if (task.get("timeWindowId") != null)
					timeWindowId = task.get("timeWindowId").toString();

			if (task.containsKey("resourceId"))
				if (task.get("resourceId") != null)
					resourceId = task.get("resourceId").toString();

			if (task.containsKey("location"))
				if (task.get("location") != null)
					location = task.get("location").toString();

			final String resourceIdFinal = resourceId;
			final String serviceIdFinal = serviceId;

			if (datasetDataObj != null && datasetDataObj.size() > 0) {
				if (resourceId != null) {
					Map<String, Object> resFiltered = null;

					try {
						resFiltered = datasetDataObj.stream()
								.filter(ds -> ds.containsKey("resourceId")
										&& ds.get("resourceId").equals(resourceIdFinal)
										&& ds.get("table_name").equals("resources"))
								.findFirst().get();
					} catch (Exception e) {
					}
					if (resFiltered != null) {
						if (resFiltered.containsKey("resourceName"))
							resourceId = resFiltered.get("resourceName") + "(" + resourceId + ")";
					}
				}

				if (serviceId != null) {
					Map<String, Object> serviceFiltered = null;

					try {
						serviceFiltered = datasetDataObj.stream()
								.filter(ds -> ds.containsKey("serviceId") && ds.get("serviceId").equals(serviceIdFinal)
										&& ds.get("table_name").equals("services"))
								.findFirst().get();
					} catch (Exception e) {

					}
					if (serviceFiltered != null) {
						if (serviceFiltered.containsKey("serviceName"))
							serviceId = serviceFiltered.get("serviceName") + "(" + serviceId + ")";
					}
				}
			}

			OptimizedJobs optimizedJobs = new OptimizedJobs();

			optimizedJobs.setJob(job);
			optimizedJobs.setTaskId(taskId);
			optimizedJobs.setTaskName(taskName);
			optimizedJobs.setStartDateTime(startDateTime);
			optimizedJobs.setEndDateTime(endDateTime);
			optimizedJobs.setTaskDuration(Integer.parseInt(taskDuration.trim()));
			optimizedJobs.setResourceLocked(Boolean.getBoolean(resourceLocked.trim()));
			optimizedJobs.setStartDateTimeLocked(Boolean.getBoolean(startDateTimeLocked.trim()));
			optimizedJobs.setCustomFieldList(null);
			optimizedJobs.setServiceId(serviceId);
			optimizedJobs.setTimeWindowId(timeWindowId);
			optimizedJobs.setResourceId(resourceId);
			optimizedJobs.setLocation(location);

			optimizedJobsList.add(optimizedJobs);

		}

		if (optimizedJobsList.size() > 0) {
			optimizedJobsRepository.saveAll(optimizedJobsList);
		}

	}

	private List<Map<String, Object>> retrieveDataSetData(Integer datasetId, Integer resourceTabId,
			Integer serviceTabId) {
		List<Object[]> dsObj = datasetDataRepository.findDataSetData(datasetId, resourceTabId, serviceTabId);

		List<Map<String, Object>> retObj = new ArrayList<>();

		if (dsObj != null)
			retObj = datasetServiceImpl.getSortedDataSet((dsObj));

		return retObj;
	}

	private List<ModelDatasetSkeletonTables> getDsTableStructure(Model model) {
		List<ModelDatasetSkeletonTables> modelDatasetSkeletonTables = modelSkeletonTablesRepository
				.findByModelIdAndStatus(model.getId(), true);
		if (modelDatasetSkeletonTables == null)
			throw new CustomException("modelDatasetSkeletonTables not found");
		else
			return modelDatasetSkeletonTables;
	}

	private Datasets retrieveDataSet(String datasetId) {

		List<Datasets> datasetList = datasetRepository.findByDatasetIdAndStatus(datasetId, true);

		if (datasetList == null || !(datasetList.size() > 0))
			throw new CustomException("Dataset not found");

		Datasets dataset = datasetList.get(0);

		return dataset;
	}

	private Jobs updateJobStatus(String jobId, String jobStatus, String completedDateTime) {

		// DateTimeFormatter formatter =
		// DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

		LocalDateTime date = null;

		date = LocalDateTime.parse(completedDateTime);

		Jobs job = jobsRepository.findByJobIdAndStatus(jobId, true);

		if (job == null)
			throw new CustomException("No job found for given job id");

		JobsStatusUpdate jobsStatusUpdate = jobsStatusRepository.findByJobId(job.getId());
		jobsStatusUpdate.setJobStatus(jobStatus);
		jobsStatusRepository.save(jobsStatusUpdate);
		job.setCompletedDateTime(date);

		job = jobsRepository.save(job);

		return job;
	}

	@Override
	public Map<String, List<Map<String, Object>>> getJobsForModel(int modelId) {
		List<Object[]> jobsList = jobsRepository.findByJobsModelId(modelId);

		if (jobsList == null)
			throw new CustomException("No Data Found");

		Map<String, List<Map<String, Object>>> jobsRes = new HashMap<>();

		List<Map<String, Object>> jobs = new ArrayList<>();

		for (Object[] row : jobsList) {

			Map<String, Object> rowMap = new HashMap<>();

			rowMap.put("id", row[0]);
			rowMap.put("model_id", row[1]);
			rowMap.put("dataset_id", row[2]);
			rowMap.put("jobId", row[3]);
			rowMap.put("job_description", row[4]);
			rowMap.put("dataset_name", row[5]);
			rowMap.put("dataset_description", row[6]);
			rowMap.put("dataset_scheduledatetime", row[7]);
			rowMap.put("completed_datetime", row[8]);
			rowMap.put("submitted_datetime", row[9]);
			rowMap.put("created_by", row[10]);
			rowMap.put("updated_by", row[11]);
			rowMap.put("createdAt", row[12]);
			rowMap.put("updatedAt", row[13]);

			jobs.add(rowMap);
		}

		jobsRes.put("jobs", jobs);
		return jobsRes;
	}

	@Override
	public List<Map<String, List<Map<String, Object>>>> fetchOptimizedJobs(String jobId) {

		List<Object[]> jobsList = jobsRepository.findOptimizedJobsByJobId(jobId);

		if (jobsList == null)
			throw new CustomException("No Data Found");

		List<Map<String, List<Map<String, Object>>>> jobListRes = new ArrayList<>();

		Map<String, List<Map<String, Object>>> jobsMap = new HashMap<>();

		List<Map<String, Object>> jobs = new ArrayList<>();

		for (Object[] row : jobsList) {

			Map<String, Object> rowMap = new HashMap<>();

			rowMap.put("job_id", row[3]);
			rowMap.put("taskId", row[4]);
			rowMap.put("taskName", row[5]);
			rowMap.put("startDateTime", row[6]);
			rowMap.put("endDateTime", row[7]);
			rowMap.put("taskDuration", row[8]);
			rowMap.put("resourceLocked", row[9]);
			rowMap.put("startDateTimeLocked", row[10]);
			rowMap.put("customFieldList", row[11]);
			rowMap.put("serviceId", row[12]);
			rowMap.put("timeWindowId", row[13]);
			rowMap.put("resourceId", row[14]);
			rowMap.put("location", row[15]);

			jobs.add(rowMap);
		}

		jobsMap.put("optimized_jobs", jobs);

		jobListRes.add(jobsMap);

		return jobListRes;

	}

	@Override
	public List<Integer> updateOptimizedJobs(Map<?, ?> req) {

		int updatedRowsCount = 0;

		List<OptimizedJobs> optimizedJobs = optimizedJobsRepository.findByTaskId(req.get("id").toString());

		for (OptimizedJobs job : optimizedJobs) {
			updatedRowsCount++;
			job.setStartDateTime(req.get("start").toString());
			job.setEndDateTime(req.get("end").toString());

			if (req.get("className").toString().equals("2")) {

				job.setResourceId(req.get("group").toString());
				job.setServiceId(req.get("content").toString());

			} else if (req.get("className").toString().equals("1")) {

				job.setResourceId(req.get("content").toString());
				job.setServiceId(req.get("group").toString());

			}
			optimizedJobsRepository.save(job);
		}

		List<Integer> res = new ArrayList<>();
		res.add(updatedRowsCount);
		return res;
	}

	@Override
	public List<Integer> deleteOptimizedJobs(Map<?, ?> req) {

		String taskId = req.get("id").toString();
		Integer totalRecords = optimizedJobsRepository.countByTaskId(taskId);

		optimizedJobsRepository.deleteByTaskId(taskId);

		List<Integer> res = new ArrayList<>();
		res.add(totalRecords);
		return res;

	}

	@Override
	public List<Map<String, List<Map<String, Object>>>> getJobsStatus(String jobId) {

		List<Object[]> jobsList = jobsRepository.findJobStatus(jobId);

		if (jobsList == null)
			throw new CustomException("No Data Found");

		Map<String, List<Map<String, Object>>> jobsRes = new HashMap<>();

		List<Map<String, Object>> jobs = new ArrayList<>();

		for (Object[] row : jobsList) {

			Map<String, Object> rowMap = new HashMap<>();

			rowMap.put("id", row[0]);
			rowMap.put("job_status", row[2]);
			rowMap.put("job_status_message", row[3]);
			rowMap.put("job_status_data", row[4]);

			jobs.add(rowMap);
		}

		jobsRes.put("jobs_statuses", jobs);
		List<Map<String, List<Map<String, Object>>>> res = new ArrayList<>();
		res.add(jobsRes);

		return res;
	}
}
