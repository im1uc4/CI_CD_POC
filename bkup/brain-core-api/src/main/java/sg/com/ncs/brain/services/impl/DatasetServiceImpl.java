package sg.com.ncs.brain.services.impl;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import sg.com.ncs.brain.entities.model.Jobs;
import sg.com.ncs.brain.entities.model.JobsSettings;
import sg.com.ncs.brain.entities.model.JobsStatusUpdate;
import sg.com.ncs.brain.entities.model.Model;
import sg.com.ncs.brain.entities.model.ModelDatasetSkeletonFields;
import sg.com.ncs.brain.entities.model.ModelDatasetSkeletonTables;
import sg.com.ncs.brain.model.dataset.DatasetData;
import sg.com.ncs.brain.model.dataset.Datasets;
import sg.com.ncs.brain.repository.DatasetDataRepository;
import sg.com.ncs.brain.repository.DatasetRepository;
import sg.com.ncs.brain.repository.JobsRepository;
import sg.com.ncs.brain.repository.JobsSettingRepository;
import sg.com.ncs.brain.repository.JobsStatusRepository;
import sg.com.ncs.brain.repository.ModelRepository;
import sg.com.ncs.brain.repository.ModelSkeletonFieldsRepository;
import sg.com.ncs.brain.repository.ModelSkeletonTablesRepository;
import sg.com.ncs.brain.services.DatasetService;
import sg.com.ncs.common.exceptions.CustomException;
import sg.com.ncs.common.exceptions.DataException;

@Service
public class DatasetServiceImpl implements DatasetService {

	@Autowired
	ModelRepository modelRepository;

	@Autowired
	DatasetRepository datasetRepository;

	@Autowired
	DatasetDataRepository datasetDataRepository;

	@Autowired
	ModelSkeletonFieldsRepository modelSkeletonFieldsRepository;

	@Autowired
	ModelSkeletonTablesRepository modelSkeletonTablesRepository;

	@Autowired
	JobsRepository jobsRepository;

	@Autowired
	JobsSettingRepository jobsSettingRepository;

	@Autowired
	JobsStatusRepository jobsStatusRepository;

	@Override
	public Datasets addDatasets(Map<?, ?> req) {

		if (!req.containsKey("dataset"))
			throw new CustomException("Dataset is missing");

		return saveJSONdataSet((Map<?, ?>) req.get("dataset"));

	}

	@Override
	public Map<?, Object> updateDatasets(Map<?, ?> req) {

		if (!req.containsKey("dataset"))
			throw new CustomException("Please provide data to save.");

		if (!req.containsKey("model_id"))
			throw new CustomException("Model Id is not specified.");

		Integer datasetId = null;
		if (req.containsKey("dataset_id"))
			datasetId = Integer.parseInt(req.get("dataset_id").toString().trim());

		Boolean isMaster = req.containsKey("Master") ? (boolean) req.get("Master") : false;

		return saveDataSet((Map<?, ?>) req.get("dataset"), Integer.parseInt(req.get("model_id").toString().trim()),
				isMaster, datasetId);

	}

	private Map<?, Object> saveDataSet(Map<?, ?> dataset, Integer modelId, Boolean isMaster, Integer datasetId) {
		DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

		LocalDateTime scheduleDate = LocalDateTime.now();
		String date = simpleDateFormat.format(scheduleDate);
		String datasetName = "DS - " + date;

		if (dataset.containsKey("datasetName"))
			datasetName = dataset.get("datasetName").toString();
		else if (datasetId != null) {

			Datasets datasetDb = datasetRepository.findById(datasetId).get();

			if (datasetDb == null)
				throw new CustomException("dataset Id provided doesn't exists, Please check");

			datasetName = datasetDb.getName();
		}

		if (dataset.containsKey("scheduleDate")) {
			String dateString = dataset.get("scheduleDate").toString();

			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

			try {
				scheduleDate = LocalDateTime.parse(dateString, format);
			} catch (Exception e) {

				try {
					format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					scheduleDate = LocalDateTime.parse(dateString, format);
				} catch (Exception ee) {

					format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
					scheduleDate = LocalDateTime.parse(dateString, format);

				}

			}
		} else if (datasetId != null) {

			Datasets datasetDb = datasetRepository.findById(datasetId).get();

			if (datasetDb == null)
				throw new CustomException("dataset Id provided doesn't exists, Please check");

			scheduleDate = datasetDb.getScheduleDateTime();
			String scheduleDateString = simpleDateFormat.format(scheduleDate);
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

			scheduleDate = LocalDateTime.parse(scheduleDateString, format);

		}

		if (!dataset.containsKey("items"))
			throw new CustomException("No Dataset items data to save.");

		List<Map<?, ?>> itemsDsObj = (List<Map<?, ?>>) dataset.get("items");

		Model model = checkModelById(modelId);

		Datasets datasetDb = insertDatasetHeader(datasetName, datasetName, datasetName, scheduleDate, model, isMaster);

		try {
			saveDataSetData(itemsDsObj, model, datasetDb);
		} catch (Exception e) {
			deletedDataset(datasetDb);
			if (e.getClass() == CustomException.class)
				throw new DataException(e.getMessage());
			else
				throw new DataException("Insert datasetdata failed, Check with tech team");
		}

		Map<?, ?> generateJob = generateJob(model, datasetDb, datasetName, scheduleDate, isMaster);

		Map<String, Object> response = new HashMap<>();

		response.put("message", "Successfully Saved");
		response.put("createdDs_id", datasetDb.getId());
		response.put("job", generateJob);

		return response;

	}

	private Map<?, ?> generateJob(Model model, Datasets datasetDb, String datasetName, LocalDateTime scheduleDate,
			Boolean isMaster) {

		LocalDateTime now = LocalDateTime.now();

		String jobId = appendZero(now.getDayOfMonth()) + appendZero(now.getMonthValue()) + appendZero(now.getYear())
				+ appendZero(now.getHour()) + appendZero(now.getMinute()) + appendZero(now.getSecond());

		Jobs jobs = new Jobs();

		jobs.setJobId(jobId);
		jobs.setDescription("Example Description");
		jobs.setModel(model);
		jobs.setDataset(datasetDb);
		jobs.setCompletedDateTime(null);

		LocalDateTime submittedDate = LocalDateTime.now();
		DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
		String formatDateTime = submittedDate.format(simpleDateFormat);
		jobs.setSubmittedDatetime(LocalDateTime.parse(formatDateTime, simpleDateFormat));
		jobs.setStatus(true);

		jobs = jobsRepository.save(jobs);

		JobsSettings jobsSettings = new JobsSettings();

		jobsSettings.setMaxjobduration(120L);
		jobsSettings.setOptimisationmodeId("BATCH");
		jobsSettings.setJob(jobs);
		jobsSettings.setMaxsolutionunimprtime(10L);

		jobsSettings = jobsSettingRepository.save(jobsSettings);

		JobsStatusUpdate jobsStatus = new JobsStatusUpdate();

		jobsStatus.setJob(jobs);
		jobsStatus.setJobStatus("QUEUING");
		jobsStatus.setJobStatusMessage("Example Job Status Message");
		jobsStatus.setJobStatusData("Example Data");

		jobsStatusRepository.save(jobsStatus);

		return generateJobResponseJson(jobId, model, datasetDb, datasetName, scheduleDate, isMaster);
	}

	private Map<?, ?> generateJobResponseJson(String jobId, Model model, Datasets datasetDb, String datasetName,
			LocalDateTime scheduleDate, Boolean isMaster) {

		Map<String, Object> dataset = new HashMap<>();
		Map<String, Object> jsonData = new HashMap<>();

		List<Object[]> rawDataset = getDataset(datasetDb);
		List<Map<String, Object>> sortedDs = getSortedDataSet(rawDataset);
		List<Map<String, String>> fieldsName = getDynamicFields(datasetDb);

		List<Map<?, ?>> resourceList = generateJsonObjects("resources", sortedDs, fieldsName, true, false);
		List<Map<?, ?>> serviceList = generateJsonObjects("services", sortedDs, fieldsName, false, false);
		List<Map<?, ?>> skillList = generateJsonObjects("skills", sortedDs, fieldsName, false, false);
		List<Map<?, ?>> taskList = generateJsonObjects("tasks", sortedDs, fieldsName, false, true);
		List<Map<?, ?>> timeWindowList = generateJsonObjects("timewindows", sortedDs, fieldsName, false, false);

		dataset.put("datasetId", datasetName);
		dataset.put("name", datasetName);
		dataset.put("description", datasetName);

//		DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
//		Instant instant = Instant.ofEpochMilli(scheduleDate.getTime());
//		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		/*
		 * LocalDate scheduleLocalDate = localDateTime.toLocalDate();
		 * 
		 * String text = scheduleLocalDate.format(formatters); LocalDate parsedDate =
		 * LocalDate.parse(text, formatters);
		 */

		dataset.put("scheduleDateTime", scheduleDate);
		dataset.put("isMasterDataset", isMaster);
		dataset.put("resourceList", resourceList);
		dataset.put("serviceList", serviceList);
		dataset.put("skillList", skillList);
		dataset.put("taskList", taskList);
		dataset.put("timeWindowList", timeWindowList);

		Map<String, Object> jobSettings = new HashMap<>();

		jobSettings.put("maxJobDuration", 120);
		jobSettings.put("optimisationMode", "BATCH");
		jobSettings.put("maxSolutionUnimprovedTime", 10);

		jsonData.put("jobId", jobId);
		jsonData.put("jobSettings", jobSettings);
		jsonData.put("dataset", dataset);

		Map<String, Object> optimisationModel = readFile("optimisationModel.json");

		if (optimisationModel == null)
			jsonData.put("optimisationModel", new HashMap<String, Object>());
		else
			jsonData.put("optimisationModel", optimisationModel.get("optimisationModel"));

		return jsonData;
	}

	private Map<String, Object> readFile(String fileName) {

		ObjectMapper mapper = new ObjectMapper();

		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream is = classloader.getResourceAsStream(fileName);
		Map<String, Object> fileMap = null;
		try {
			fileMap = mapper.readValue(is, new TypeReference<Map<String, Object>>() {
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

		return fileMap;
	}

	private List<Map<?, ?>> generateJsonObjects(String groupName, List<Map<String, Object>> sortedDs,
			List<Map<String, String>> fieldsName, boolean checkResorceTimeWindowIdList,
			boolean checkTaskTimeWindowIdList) {
		List<Map<?, ?>> dynamicArrayObject = new ArrayList<>();

		for (Map<String, Object> ds : sortedDs) {

			if (ds.get("table_name").equals(groupName)) {

				List<Map<String, Object>> customFieldList = new ArrayList<>();

				for (Map<String, String> fields : fieldsName) {
					if (fields.get("table_name").equals(groupName) && (Boolean.parseBoolean(fields.get("isDynamic")))
							&& !fields.get("control_type").equals("button")) {

						Map<String, Object> customField = new HashMap<>();

						if (fields.get("column_type").toUpperCase().equals("TEXT")) {
							customField.put("name", fields.get("column_name"));
							customField.put("customFieldType", fields.get("column_type").toUpperCase());
							customField.put("textValue", ds.get(fields.get("column_name")));

						}

						if (fields.get("column_type").toUpperCase().equals("NUMERIC")) {
							customField.put("name", fields.get("column_name"));
							customField.put("customFieldType", fields.get("column_type").toUpperCase());
							customField.put("textValue", ds.get(fields.get("column_name")));
							customField.put("isInteger", true);
						}

						if (fields.get("column_type").toUpperCase().equals("DATE_TIME")) {
							customField.put("name", fields.get("column_name"));
							customField.put("customFieldType", fields.get("column_type").toUpperCase());
							customField.put("dateTimeValue", ds.get(fields.get("column_name")));
						}
						customFieldList.add(customField);
					}

				}

				Map<String, Object> dynamicArrayElement = new HashMap<>();
				for (Map<String, String> fields : fieldsName) {
					if (fields.get("table_name").equals(groupName) && !(Boolean.parseBoolean(fields.get("isDynamic")))
							&& (!fields.get("control_type").equals("button"))) {

						dynamicArrayElement.put(fields.get("column_name"), ds.get(fields.get("column_name")));
					}
				}
				dynamicArrayElement.put("customFieldList", customFieldList);

				if (checkResorceTimeWindowIdList) {
					List<Object> timeWindowIdList = new ArrayList<>();
					for (Map<String, Object> newDs : sortedDs) {
						if (newDs.get("table_name").toString().equals("resources_timewindows")
								&& ds.containsKey("resourceId")
								&& ds.get("resourceId").equals(newDs.get("resourceId"))) {
							timeWindowIdList.add(newDs.get("timeWindowId"));
						}
					}
					dynamicArrayElement.put("timeWindowIdList", timeWindowIdList);
				}

				if (checkTaskTimeWindowIdList) {
					List<Object> timeWindowIdList = new ArrayList<>();
					for (Map<String, Object> newDs : sortedDs) {
						if (newDs.get("table_name").equals("tasks_timewindows") && ds.containsKey("taskId")
								&& ds.get("taskId").equals(newDs.get("taskId"))) {
							timeWindowIdList.add(newDs.get("timeWindowId"));
						}
					}

					dynamicArrayElement.put("timeWindowIdList", timeWindowIdList);
				}

				dynamicArrayObject.add(dynamicArrayElement);
			}
		}
		return dynamicArrayObject;
	}

	private List<Map<String, String>> getDynamicFields(Datasets datasetDb) {
		List<Object[]> dynamicList = datasetDataRepository.findDynamicFields(datasetDb.getId());

		List<Map<String, String>> dynamicField = new ArrayList<>();

		for (Object[] field : dynamicList) {

			Map<String, String> dynamicMap = new HashMap<>();

			dynamicMap.put("column_name", field[0].toString());
			dynamicMap.put("column_type", field[1].toString());
			dynamicMap.put("control_type", field[2].toString());
			dynamicMap.put("isDynamic", field[3].toString());
			dynamicMap.put("table_name", field[4].toString());

			dynamicField.add(dynamicMap);
		}

		return dynamicField;
	}

	public List<Map<String, Object>> getSortedDataSet(List<Object[]> rawDataset) {
		// recordid 0
		// table name 1
		// column name 2
		// value 3

		List<Map<String, Object>> dsListSorted = new ArrayList<>();
		Map<String, Map<String, Object>> dsMap = new HashMap<>();
		for (Object[] obj : rawDataset) {

			if (!dsMap.containsKey(obj[0].toString())) {
				Map<String, Object> tempMap = new HashMap<>();
				tempMap.put("record_id", obj[0].toString());
				tempMap.put("table_name", obj[1].toString());
				// if (obj[3] != null)
				tempMap.put(obj[2].toString(), obj[3]);
//				else
//					tempMap.put(obj[2].toString(), "null");
				dsMap.put(obj[0].toString(), tempMap);
				dsListSorted.add(dsMap.get(obj[0].toString()));
			} else {

//				dsMap.get(obj[0].toString()).put(obj[2].toString(), obj[3].toString());

				// if (obj[3] != null)
				dsMap.get(obj[0].toString()).put((obj[2].toString()).toString(), obj[3]);
//				else
//					dsMap.get(obj[0].toString()).put((obj[2].toString()).toString(), "null");
			}

		}

		return dsListSorted;
	}

	private List<Object[]> getDataset(Datasets datasetDb) {
		return datasetDataRepository.findDataset(datasetDb.getId());
	}

	private String appendZero(int value) {
		if (value < 10) {
			return "0" + String.valueOf(value);
		} else {
			return String.valueOf(value);
		}
	}

	private void saveDataSetData(List<Map<?, ?>> itemsDsObj, Model model, Datasets datasetDb) {

		List<Map<String, String>> rawDs = convertRawDataSet(itemsDsObj);
		List<ModelDatasetSkeletonFields> dsSkeletonFields = getDsFieldStructure();
		List<ModelDatasetSkeletonTables> dsSkeletonTables = getDsTableStructure(model);

		Map<String, List<Map<String, String>>> groupedDsTableName = groupDsByTableName(rawDs);

		for (String tableName : groupedDsTableName.keySet()) {

			List<DatasetData> finalObj = new ArrayList<>();
			ModelDatasetSkeletonTables modelDatasetSkeletonTables = (ModelDatasetSkeletonTables) getStructureID(
					dsSkeletonTables, dsSkeletonFields, tableName, null, model, "table");

			if (modelDatasetSkeletonTables == null)
				throw new CustomException("check data tableName : " + tableName + " for model id  : " + model.getId());

			List<Map<String, String>> tableObject = groupedDsTableName.get(tableName);

			for (Map<String, String> elements : tableObject) {

				// check field parameter
				for (String field : elements.keySet()) {
					ModelDatasetSkeletonFields modelDatasetSkeletonFields = (ModelDatasetSkeletonFields) getStructureID(
							dsSkeletonTables, dsSkeletonFields, modelDatasetSkeletonTables.getTableName(), field, model,
							"field");

					// if (modelDatasetSkeletonFields == null)
					/*
					 * System.out.println("for testing purpose only : check data tableName : " +
					 * tableName + " for model id  : " + model.getId() + " for field  : " + field);
					 */

					/*
					 * if (modelDatasetSkeletonFields == null) throw new
					 * CustomException("check data tableName : " + tableName + " for model id  : " +
					 * model.getId() + " for field  : " + field);
					 */

					DatasetData datasetData = buildBulkObject(new ArrayList<DatasetData>(), field, elements.get(field),
							modelDatasetSkeletonTables, modelDatasetSkeletonFields,
							Integer.parseInt(elements.get("record_id")), datasetDb);

					if (datasetData != null) {
						// throw new CustomException("Error in bulk object creation, please check");
						finalObj.add(datasetData);
					}
				}

			}

//			List<DatasetData> datasetDataList = null;
			if (finalObj != null && finalObj.size() > 0)
				/* datasetDataList = */insertDsData(finalObj);
			/*
			 * if (datasetDataList == null && (finalObj != null && !(finalObj.size() > 0)))
			 * throw new CustomException("DatasetData Bulk Save Exception");
			 */
		}

	}

	private Map<String, List<Map<String, String>>> groupDsByTableName(List<Map<String, String>> rawDs) {

		Map<String, List<Map<String, String>>> groupedDs = new HashMap<>();
		rawDs.forEach(ds -> {
			Map<String, String> dsMap = new HashMap<>();
			List<Map<String, String>> dsList = new ArrayList<>();
			ds.keySet().forEach(key -> {
				if (key != "table_name")
					dsMap.put(key, ds.get(key));
			});
			dsList.add(dsMap);
			if (groupedDs.containsKey(ds.get("table_name"))) {
				dsList = groupedDs.get(ds.get("table_name").toString());
				dsList.add(dsMap);
			}
			groupedDs.put(ds.get("table_name").toString(), dsList);
		});
		return groupedDs;
	}

	private List<Map<String, String>> convertRawDataSet(List<Map<?, ?>> itemsDsObj) {

		List<Map<String, String>> rawDs = new ArrayList<>();

		itemsDsObj.forEach(item -> {

			Map<String, Map<String, String>> itemMap = new HashMap<>();

			// item.get("record_id");

			if (!itemMap.containsKey(item.get("record_id"))) {

				Map<String, String> insideItem = new HashMap<>();

				insideItem.put("record_id", item.get("record_id").toString());
				insideItem.put("table_name", item.get("table_name").toString());
				insideItem.put(item.get("field_name").toString(), item.get("value").toString());

				itemMap.put(item.get("record_id").toString(), insideItem);

				if (item.containsKey("_createdByApp"))
					rawDs.add(0, itemMap.get(item.get("record_id").toString()));
				else
					rawDs.add(itemMap.get(item.get("record_id").toString()));

			} else {
				itemMap.get(item.get("record_id")).put(item.get("field_name").toString(), item.get("value").toString());
			}

		});

		return rawDs;
	}

	private Datasets saveJSONdataSet(Map<?, ?> dataset) {

		checkJSONMainComponent(dataset);

		String datasetName = dataset.containsKey("name") ? dataset.get("name").toString() : "Temp Name for InstanceDS";
		String datasetId = dataset.containsKey("datasetId") ? dataset.get("datasetId").toString()
				: "Temp ID for InstanceDS";
		String description = dataset.containsKey("description") ? dataset.get("description").toString()
				: "Temp Description";

		LocalDateTime scheduleDateTime = null;
		// DateTimeFormatter simpleDateFormat =
		// DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
		if (dataset.containsKey("scheduleDateTime")) {
			scheduleDateTime = LocalDateTime.parse(dataset.get("scheduleDateTime").toString());

		} else {
			scheduleDateTime = LocalDateTime.now();

		}
		Boolean isMasterDataset = null;
		if (dataset.containsKey("isMasterDataset"))
			isMasterDataset = (Boolean) dataset.get("isMasterDataset");
		else
			isMasterDataset = false;

		Model model = checkModelByOptimisationModelId(dataset.get("optimisationModelId").toString());

		// Insert header
		Datasets datasetDb = insertDatasetHeader(datasetName, datasetId, description, scheduleDateTime, model,
				isMasterDataset);

		List<DatasetData> datasetDataDb = null;
		try {
			datasetDataDb = insertDataSetData(dataset, model, datasetDb);
		} catch (Exception e) {
			deletedDataset(datasetDb);
			if (e.getClass() == CustomException.class)
				throw new DataException(e.getMessage());
			else
				throw new DataException("Insert datasetdata failed, Check with tech team");
		}
		if (datasetDataDb == null) {
			deletedDataset(datasetDb);
			throw new CustomException("Insert datasetdata failed");
		} else
			return datasetDb;
	}

	private void deletedDataset(Datasets datasetDb) {
		datasetRepository.delete(datasetDb);
	}

	private List<DatasetData> insertDataSetData(Map<?, ?> dataset, Model model, Datasets datasetDb) {
		boolean listExists = false;
		int recordId = 1;

		List<DatasetData> finalObj = new ArrayList<>();

		List<ModelDatasetSkeletonFields> dsSkeletonFields = getDsFieldStructure();
		List<ModelDatasetSkeletonTables> dsSkeletonTables = getDsTableStructure(model);

		if (dataset.containsKey("timeWindowList")) {
			listExists = true;

			List<Map<?, ?>> timeWindowList = (List<Map<?, ?>>) dataset.get("timeWindowList");

			recordId = insertSegmentData(finalObj, timeWindowList, dsSkeletonTables, dsSkeletonFields, "timeWindowList",
					model, datasetDb, recordId);
			++recordId;
		}

		if (dataset.containsKey("resourceList")) {
			listExists = true;
			List<Map<?, ?>> resourceList = (List<Map<?, ?>>) dataset.get("resourceList");

			recordId = insertSegmentData(finalObj, resourceList, dsSkeletonTables, dsSkeletonFields, "resourceList",
					model, datasetDb, recordId);
			++recordId;
		}

		if (dataset.containsKey("serviceList")) {
			listExists = true;
			List<Map<?, ?>> serviceList = (List<Map<?, ?>>) dataset.get("serviceList");

			recordId = insertSegmentData(finalObj, serviceList, dsSkeletonTables, dsSkeletonFields, "serviceList",
					model, datasetDb, recordId);
			++recordId;
		}

		if (dataset.containsKey("skillList")) {
			listExists = true;
			List<Map<?, ?>> skillList = (List<Map<?, ?>>) dataset.get("skillList");

			recordId = insertSegmentData(finalObj, skillList, dsSkeletonTables, dsSkeletonFields, "skillList", model,
					datasetDb, recordId);
			++recordId;
		}

		if (dataset.containsKey("taskList")) {
			listExists = true;
			List<Map<?, ?>> taskList = (List<Map<?, ?>>) dataset.get("taskList");

			recordId = insertSegmentData(finalObj, taskList, dsSkeletonTables, dsSkeletonFields, "taskList", model,
					datasetDb, recordId);
			++recordId;
		}

		if (finalObj.size() > 0) {
			List<DatasetData> datasetDataList = insertDsData(finalObj);

			if (datasetDataList == null)
				throw new CustomException("DatasetData Bulk Save Exception");
			return datasetDataList;
		}

		return null;

	}

	private List<DatasetData> insertDsData(List<DatasetData> finalObj) {
		if (finalObj.size() > 0)
			return datasetDataRepository.saveAll(finalObj);
		return null;
	}

	private int insertSegmentData(List<DatasetData> finalObj, List<Map<?, ?>> objList,
			List<ModelDatasetSkeletonTables> dsSkeletonTables, List<ModelDatasetSkeletonFields> dsSkeletonFields,
			String dataType, Model model, Datasets datasetDb, int recordId) {

		String tableName = null;

		if (dataType.equals("locationList"))
			tableName = "location";
		if (dataType.equals("resourceList"))
			tableName = "resources";
		if (dataType.equals("serviceList"))
			tableName = "services";
		if (dataType.equals("skillList"))
			tableName = "skills";
		if (dataType.equals("taskList"))
			tableName = "tasks";
		if (dataType.equals("timeWindowList"))
			tableName = "timeWindows";

		ModelDatasetSkeletonTables modelDatasetSkeletonTables = (ModelDatasetSkeletonTables) getStructureID(
				dsSkeletonTables, dsSkeletonFields, tableName, null, model, "table");

		if (modelDatasetSkeletonTables == null)
			throw new CustomException(
					"Error while fetching table structure , please check Table: " + tableName + " in dataset skeleton");

		for (Map<?, ?> obj : objList) {

			// System.out.println(obj);
			/*
			 * Map<?, ?> timewindowField = null;
			 */
			Integer timeWindowRecordId = null;

			for (Object key : obj.keySet()) {

				String elementName;
				ModelDatasetSkeletonFields modelDatasetSkeletonFields = null;
				// hardcoded columns for tasks and resources
				if ((tableName.equals("resources")) || (tableName.equals("tasks"))) {

					modelDatasetSkeletonFields = (ModelDatasetSkeletonFields) getStructureID(dsSkeletonTables,
							dsSkeletonFields, modelDatasetSkeletonTables.getTableName(), "IsTimeWindowExists", model,
							"field");

					if (modelDatasetSkeletonFields == null)
						throw new CustomException("Error while fetching table structure , please check if Table: "
								+ modelDatasetSkeletonTables.getTableName()
								+ " , Field: 'IsTimeWindowExists' is specified in the dataset skeleton?");

					buildBulkObject(finalObj, "IsTimeWindowExists", "View Availability", modelDatasetSkeletonTables,
							modelDatasetSkeletonFields, recordId, datasetDb);

					// if resourcelocked false, need to insert extra resourceId column[start]
					if (key.equals("resourceLocked") && !obj.containsKey(("resourceId"))) {

						modelDatasetSkeletonFields = (ModelDatasetSkeletonFields) getStructureID(dsSkeletonTables,
								dsSkeletonFields, modelDatasetSkeletonTables.getTableName(), "resourceId", model,
								"field");

						if (modelDatasetSkeletonFields == null)
							throw new CustomException("Error while fetching table structure , please check if Table: "
									+ tableName + " , Field: " + key + " is specified in the dataset skeleton?");

						buildBulkObject(finalObj, "resourceId", "", modelDatasetSkeletonTables,
								modelDatasetSkeletonFields, recordId, datasetDb);
					}
				}

				if (tableName.equals("resources")) {

					modelDatasetSkeletonFields = (ModelDatasetSkeletonFields) getStructureID(dsSkeletonTables,
							dsSkeletonFields, modelDatasetSkeletonTables.getTableName(), "IsSkillsExists", model,
							"field");

					if (modelDatasetSkeletonFields == null)
						throw new CustomException("Error while fetching table structure , please check if Table: "
								+ tableName + " , Field: " + key + " is specified in the dataset skeleton?");

					buildBulkObject(finalObj, "IsSkillsExists", "View Capabilities", modelDatasetSkeletonTables,
							modelDatasetSkeletonFields, recordId, datasetDb);

				}

				if (key.equals("timeWindowIdList") && (tableName.equals("resources") || (tableName.equals("tasks")))) {
					elementName = (tableName.equals("resources") ? "resourceId"
							: (tableName.equals("tasks") ? "taskId" : ""));

					timeWindowRecordId = insertTimeWindowSegmentData(finalObj, (List<String>) obj.get(key),
							obj.get(elementName).toString(), dsSkeletonTables, dsSkeletonFields, tableName, datasetDb,
							recordId, model);

					continue;
				}

				if (key.equals("customFieldList")) {
					insertCustomSegmentData(finalObj, (List<Map<?, ?>>) obj.get(key), dsSkeletonTables,
							dsSkeletonFields, tableName, datasetDb, model, recordId);
					continue;
				}

				modelDatasetSkeletonFields = (ModelDatasetSkeletonFields) getStructureID(dsSkeletonTables,
						dsSkeletonFields, modelDatasetSkeletonTables.getTableName(), key.toString(), model, "field");

				if (modelDatasetSkeletonFields == null)
					throw new CustomException("Error while fetching table structure , please check if Table: "
							+ modelDatasetSkeletonTables.getTableName() + " , Field: " + key
							+ " is specified in the dataset skeleton?");

				buildBulkObject(finalObj, key.toString(), obj.get(key).toString(), modelDatasetSkeletonTables,
						modelDatasetSkeletonFields, recordId, datasetDb);

			}

			if (timeWindowRecordId != null) {
				recordId = timeWindowRecordId + 1;
			} else
				recordId++;
		}
		return recordId;
	}

	private void insertCustomSegmentData(List<DatasetData> finalObj, List<Map<?, ?>> jsonObject,
			List<ModelDatasetSkeletonTables> dsSkeletonTables, List<ModelDatasetSkeletonFields> dsSkeletonFields,
			String tableName, Datasets datasetDb, Model model, int recordId) {

		ModelDatasetSkeletonTables modelDatasetSkeletonTables = (ModelDatasetSkeletonTables) getStructureID(
				dsSkeletonTables, dsSkeletonFields, tableName, null, model, "table");

		for (Map<?, ?> element : jsonObject) {

			if (!element.containsKey("name"))
				throw new CustomException("Custom Field Data Structure is incomplete, name is missing.");

			if (!element.containsKey("customFieldType"))
				throw new CustomException("Custom Field Data Structure is incomplete.,customFieldType is missing.");

			String valueFieldName = (element.get("customFieldType").equals("TEXT") ? "textValue"
					: (element.get("customFieldType").equals("NUMERIC") ? "numericValue" : "Value"));

			if (!element.containsKey(valueFieldName))
				throw new CustomException(
						"Custom Field Data Structure is incomplete , " + valueFieldName + " is missing.");

			ModelDatasetSkeletonFields modelDatasetSkeletonFields = (ModelDatasetSkeletonFields) getStructureID(
					dsSkeletonTables, dsSkeletonFields, tableName, element.get("name").toString(), model, "field");

			if (modelDatasetSkeletonFields == null) {

				throw new CustomException(
						"Table: " + tableName + " , Field: " + element + " not specified in the dataset skeleton.");
			}

			buildBulkObject(finalObj, element.get("name").toString(), element.get(valueFieldName).toString(),
					modelDatasetSkeletonTables, modelDatasetSkeletonFields, recordId, datasetDb);

		}

	}

	private int insertTimeWindowSegmentData(List<DatasetData> finalObj, List<String> jsonObject, String value,
			List<ModelDatasetSkeletonTables> dsSkeletonTables, List<ModelDatasetSkeletonFields> dsSkeletonFields,
			String tableName, Datasets datasetDb, int recordId, Model model) {

		String targetTableName = (tableName.equals("resources") ? "resources_timewindows"
				: (tableName.equals("tasks") ? "tasks_timewindows" : ""));

		recordId += 1000000;

		for (String elnt : jsonObject) {
			String elementName;
			ModelDatasetSkeletonFields modelDatasetSkeletonFields = null;

			ModelDatasetSkeletonTables modelDatasetSkeletonTables = (ModelDatasetSkeletonTables) getStructureID(
					dsSkeletonTables, dsSkeletonFields, targetTableName, null, model, "table");

			if (modelDatasetSkeletonTables == null)
				throw new CustomException("Error while fetching table structure , please check Table: " + tableName
						+ " in dataset skeleton");

			elementName = (tableName.equals("resources") ? "resourceId" : (tableName.equals("tasks") ? "taskId" : ""));
			modelDatasetSkeletonFields = (ModelDatasetSkeletonFields) getStructureID(dsSkeletonTables, dsSkeletonFields,
					targetTableName, elementName, model, "field");

			if (modelDatasetSkeletonFields == null)
				throw new CustomException("Error while fetching table structure , please check if Table: "
						+ targetTableName + " , Field: " + elementName + " is specified in the dataset skeleton?");

			buildBulkObject(finalObj, elementName, value, modelDatasetSkeletonTables, modelDatasetSkeletonFields,
					recordId, datasetDb);

			modelDatasetSkeletonFields = (ModelDatasetSkeletonFields) getStructureID(dsSkeletonTables, dsSkeletonFields,
					targetTableName, "timeWindowId", model, "field");

			if (modelDatasetSkeletonFields == null)
				throw new CustomException("Error while fetching table structure , please check if Table: " + tableName
						+ " , Field: timeWindowId is specified in the dataset skeleton?");

			buildBulkObject(finalObj, "timeWindowId", elnt.toString(), modelDatasetSkeletonTables,
					modelDatasetSkeletonFields, recordId, datasetDb);

			recordId++;

		}

		return recordId;

	}

	private DatasetData buildBulkObject(List<DatasetData> finalObj, String elementName, String elementValue,
			ModelDatasetSkeletonTables dsSkeletonTable, ModelDatasetSkeletonFields dsSkeletonField, int recordId,
			Datasets datasetDb) {

		if (dsSkeletonField != null && dsSkeletonTable != null && elementName != null) {
			DatasetData dsData = new DatasetData();
			if (elementValue == null)
				elementValue = "";

			if (elementName.contains("endDateTime") || elementName.contains("startDateTime")) {

				// check nodejs
			}
			if (elementName.contains("resourceLocked") || elementName.contains("startDateTimeLocked")) {

				// check nodjs
				// not required if Boolean.toString() handled before itself
			}

			dsData.setDataset(datasetDb);
			dsData.setDatasetSkeletonField(dsSkeletonField);
			dsData.setDatasetSkeletonTable(dsSkeletonTable);
			dsData.setRecordId(recordId);
			dsData.setValue(elementValue);
			dsData.setStatus(true);

			finalObj.add(dsData);
			return dsData;
		}

		return null;
	}

	private Object getStructureID(List<ModelDatasetSkeletonTables> dsSkeletonTables,
			List<ModelDatasetSkeletonFields> dsSkeletonFields, String tableName, String columnName, Model model,
			String type) {

		if (type.equals("field")) {

			ModelDatasetSkeletonTables dsSkeletonTable = dsSkeletonTables.stream()
					.filter(table -> table.getTableName().equalsIgnoreCase(tableName)
							&& table.getModel().getId() == model.getId())
					.findFirst().orElse(null);

			ModelDatasetSkeletonFields dsSkeletonField = dsSkeletonFields.stream()
					.filter(field -> field.getTable().getId() == dsSkeletonTable.getId()
							&& field.getColumnName().equalsIgnoreCase(columnName) && field.getStatus() == true)
					.findFirst().orElse(null);

			return dsSkeletonField;

		} else if (type.equals("table")) {

			ModelDatasetSkeletonTables dsSkeletonTable = dsSkeletonTables.stream()
					.filter(table -> table.getTableName().equalsIgnoreCase(tableName)
							&& table.getModel().getId() == model.getId() && table.getStatus() == true)
					.findFirst().orElse(null);

			return dsSkeletonTable;

		}
		return null;
	}

	private List<ModelDatasetSkeletonTables> getDsTableStructure(Model model) {

		List<ModelDatasetSkeletonTables> dsSkeletonTables = modelSkeletonTablesRepository
				.findByModelIdAndStatus(model.getId(), true);

		if (dsSkeletonTables == null)
			throw new CustomException("Model dataset skeleton tables not found");

		return dsSkeletonTables;
	}

	private List<ModelDatasetSkeletonFields> getDsFieldStructure() {
		List<ModelDatasetSkeletonFields> dsSkeletonFields = modelSkeletonFieldsRepository.findByStatus(true);

		if (dsSkeletonFields == null)
			throw new CustomException("Model dataset skeleton fields not found");

		return dsSkeletonFields;
	}

	private Datasets insertDatasetHeader(String datasetName, String datasetId, String description,
			LocalDateTime scheduleDateTime, Model model, Boolean isMasterDataset) {

		if (!datasetName.equals(datasetId))
			checkDatasetExists(datasetName, datasetId, isMasterDataset);

		Datasets dataset = new Datasets();
		dataset.setDatasetId(datasetId);
		dataset.setName(datasetName);
		dataset.setDescription(description);
		dataset.setModel(model);
		dataset.setIsMasterds(isMasterDataset);
		dataset.setScheduleDateTime(scheduleDateTime);
		dataset.setStatus(true);

		return datasetRepository.save(dataset);
	}

	private Boolean checkDatasetExists(String datasetName, String datasetId, Boolean isMasterDataset) {

		if (isMasterDataset) {

			Boolean datasetExists = datasetRepository.existsByDatasetIdAndNameAndIsMasterdsAndStatus(datasetId,
					datasetName, isMasterDataset, true);

			if (datasetExists)
				throw new CustomException("Dataset Name is not unique");

			return true;
		} else
			return true;
	}

	private Boolean checkJSONMainComponent(Map<?, ?> dataset) {

		if (!dataset.containsKey("optimisationModelId"))
			throw new CustomException("optimisationModelId is missing");

		checkModelByOptimisationModelId(dataset.get("optimisationModelId").toString());

		if (!dataset.containsKey("resourceList"))
			throw new CustomException("resourceList is missing");

		if (!dataset.containsKey("serviceList"))
			throw new CustomException("serviceList is missing");

		if (!dataset.containsKey("skillList"))
			throw new CustomException("skillList is missing");

		if (!dataset.containsKey("taskList"))
			throw new CustomException("taskList is missing");

		return true;
	}

	private Model checkModelByOptimisationModelId(String optimisationModelId) {

		Model model = modelRepository.findByOptimisationModelIdAndModelStatus(optimisationModelId, true);

		if (model == null)
			throw new CustomException("Specified Model doesn't exists in the DB");

		return model;
	}

	private Model checkModelById(Integer id) {

		Model model = modelRepository.findById(id).orElse(null);
		if (model == null)
			throw new CustomException("Specified Model doesn't exists in the DB");

		return model;
	}

	@Override
	public Map<String, List<Map<String, Object>>> getDsForModel(Integer modelId) {

		List<Object[]> dsList = datasetRepository.findByDsModelId(modelId);

		if (dsList == null)
			throw new CustomException("No Data Found");

		Map<String, List<Map<String, Object>>> dsRes = new HashMap<>();

		List<Map<String, Object>> datasets = new ArrayList<>();

		for (Object[] row : dsList) {

			Map<String, Object> rowMap = new HashMap<>();

			rowMap.put("id", row[0]);
			rowMap.put("model_id", row[1]);
			rowMap.put("dataset_id", row[2]);
			rowMap.put("dataset_name", row[3]);
			rowMap.put("isMasterdataset", row[4]);
			rowMap.put("dataset_description", row[5]);
			rowMap.put("dataset_scheduledatetime", row[6]);
			rowMap.put("created_by", row[7]);
			rowMap.put("updated_by", row[8]);
			rowMap.put("createdAt", row[9]);
			rowMap.put("updatedAt", row[10]);

			datasets.add(rowMap);
		}

		dsRes.put("ds_lists", datasets);
		return dsRes;

	}

	@Override
	public Map<String, List<Map<String, Object>>> getRawDataById(Integer datasetId) {

		List<Object[]> dsList = datasetRepository.findRawDs(datasetId);

		if (dsList == null)
			throw new CustomException("No Data Found");

		Map<String, List<Map<String, Object>>> dsRes = new HashMap<>();

		List<Map<String, Object>> datasets = new ArrayList<>();

		for (Object[] row : dsList) {

			Map<String, Object> rowMap = new HashMap<>();

			rowMap.put("id", row[0]);
			rowMap.put("record_id", row[1]);
			rowMap.put("table_name", row[2]);
			rowMap.put("table_display_name", row[3]);
			rowMap.put("dataset_id", row[4]);
			rowMap.put("field_name", row[5]);
			rowMap.put("value", row[6]);
			rowMap.put("status", Boolean.valueOf(row[7].toString()) == true ? 1 : 0);
			rowMap.put("created_by", row[8]);
			rowMap.put("updated_by", row[9]);
			rowMap.put("createdAt", row[10]);
			rowMap.put("updatedAt", row[11]);

			datasets.add(rowMap);
		}

		dsRes.put("dataset", datasets);
		return dsRes;

	}
}
