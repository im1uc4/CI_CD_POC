package sg.com.ncs.brain.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.com.ncs.brain.entities.model.GeneralFieldsTemplates;
import sg.com.ncs.brain.entities.model.Model;
import sg.com.ncs.brain.entities.model.ModelDatasetSkeletonFields;
import sg.com.ncs.brain.entities.model.ModelDatasetSkeletonTables;
import sg.com.ncs.brain.repository.ModelRepository;
import sg.com.ncs.brain.repository.ModelSkeletonFieldsRepository;
import sg.com.ncs.brain.repository.ModelSkeletonTablesRepository;
import sg.com.ncs.brain.services.SkeletonService;
import sg.com.ncs.common.exceptions.CustomException;

@Service
public class SkeletonServiceImpl implements SkeletonService {

	@Autowired
	ModelRepository modelRepository;

	@Autowired
	ModelSkeletonTablesRepository modelSkeletonTablesRepository;

	@Autowired
	ModelSkeletonFieldsRepository modelSkeletonFieldsRepository;

	@Override
	public GeneralFieldsTemplates addSkeleton(Map<?, ?> req) {

		if (!req.containsKey("skeleton"))
			throw new CustomException("skeleton key is missing in the JSON body.");

		saveJsonMSkeleton((Map<?, ?>) req.get("skeleton"));

		return null;
	}

	@SuppressWarnings("unchecked")
	private void saveJsonMSkeleton(Map<?, ?> skeleton) {
		if (!skeleton.containsKey("optimisationModelId"))
			throw new CustomException("optimisationModelId key is missing in the JSON body.");

		if (!skeleton.containsKey("skeletonFields"))
			throw new CustomException("skeletonFields key is missing in the JSON body.");

		String optModelId = skeleton.get("optimisationModelId").toString();
		List<Map<?, ?>> skeletonFields = (List<Map<?, ?>>) skeleton.get("skeletonFields");

		Model model = isModelExists(optModelId);

		List<ModelDatasetSkeletonFields> skeletonFieldsList = new ArrayList<>();

		skeletonFields.forEach(field -> {

			checkFieldSegment(field, model);

			String table = field.get("table").toString();
			String columnName = field.get("column_name").toString();
			String displayName = field.get("display_name").toString();
			String columnType = field.get("column_type").toString();
			String controlType = field.get("control_type").toString();

			String columnFormat = field.get("column_format").toString();
			Boolean isMandatory = (Boolean) field.get("is_mandatory");
			Boolean isVisible = (Boolean) field.get("is_visible");

			ModelDatasetSkeletonTables modelDatasetSkeletonTables = modelSkeletonTablesRepository
					.findByModelIdAndTableNameAndStatus(model.getId(), table, true);

			if (modelDatasetSkeletonTables == null)
				throw new CustomException(
						"The dataset skeleton table for the table id mentioned, dosent exists, Please check");
			// check this query
			ModelDatasetSkeletonFields modelDatasetSkeletonFields = modelSkeletonFieldsRepository
					.findTopByTableIdOrderByFieldSeqNoDesc(modelDatasetSkeletonTables.getId());

			Integer maximumSeq = null;

			if (modelDatasetSkeletonFields == null)
				maximumSeq = 0;
			else
				maximumSeq = modelDatasetSkeletonFields.getFieldSeqNo();

			// Previous logic
//			if (skeletonFieldsList.size() == 0) {
//				maximumSeq++;
//			} else {
//				maximumSeq = maximumSeq
//						+ (int) skeletonFieldsList.stream().filter(skeletonField -> skeletonField
//								.getModelDatasetSkeletonTable().getId() == modelDatasetSkeletonTables.getId()).count()
//						+ 1;
//			}

			// New logic
			if (skeletonFieldsList.stream().filter(skeletonField -> skeletonField.getTable()
					.getId() == modelDatasetSkeletonTables.getId()).count() == 0) {
				maximumSeq++;
			} else {
				maximumSeq = maximumSeq
						+ (int) skeletonFieldsList.stream().filter(skeletonField -> skeletonField
								.getTable().getId() == modelDatasetSkeletonTables.getId()).count()
						+ 1;
			}

			ModelDatasetSkeletonFields modelDatasetSkeletonFieldsContinued = new ModelDatasetSkeletonFields();

			modelDatasetSkeletonFieldsContinued.setColumnFormat(columnFormat);
			modelDatasetSkeletonFieldsContinued.setStatus(true);
			modelDatasetSkeletonFieldsContinued.setTable(modelDatasetSkeletonTables);
			modelDatasetSkeletonFieldsContinued.setColumnName(columnName);
			modelDatasetSkeletonFieldsContinued.setControlType(controlType);
			modelDatasetSkeletonFieldsContinued.setColumnType(columnType);
			modelDatasetSkeletonFieldsContinued.setDisplayName(displayName);
			modelDatasetSkeletonFieldsContinued.setFieldSeqNo(maximumSeq);
			modelDatasetSkeletonFieldsContinued.setIsDynamic(true);
			modelDatasetSkeletonFieldsContinued.setIsMandatory(isMandatory);
			modelDatasetSkeletonFieldsContinued.setIsVisible(isVisible);

			skeletonFieldsList.add(modelDatasetSkeletonFieldsContinued);

		});

		modelSkeletonFieldsRepository.saveAll(skeletonFieldsList);

	}

	private Model isModelExists(String optModelId) {
		Boolean isModelExist = modelRepository.existsByOptimisationModelIdAndModelStatus(optModelId, true);

		if (!isModelExist)
			throw new CustomException(
					"Optimization Model " + optModelId + " is missing in the DB. To proceed, please import model.");

		Model model = modelRepository.findByOptimisationModelIdAndModelStatus(optModelId, true);
		return model;
	}

	private boolean checkFieldSegment(Map<?, ?> field, Model model) {

		if (!field.containsKey("table"))
			throw new CustomException("table key is missing");
		if (!field.containsKey("column_name"))
			throw new CustomException("column_name key is missing");
		if (!field.containsKey("display_name"))
			throw new CustomException("display_name key is missing");
		if (!field.containsKey("column_type"))
			throw new CustomException("column_type key is missing");
		if (!field.containsKey("control_type"))
			throw new CustomException("control_type key is missing");
		if (!field.containsKey("column_format"))
			throw new CustomException("column_format key is missing");
		if (!field.containsKey("is_mandatory"))
			throw new CustomException("is_mandatory key is missing");
		if (!field.containsKey("is_visible"))
			throw new CustomException("is_visible key is missing");

		String table = field.get("table").toString();
		String columnName = field.get("column_name").toString();
		String displayName = field.get("display_name").toString();
		String columnType = field.get("column_type").toString();
		String controlType = field.get("control_type").toString();
		Boolean isMandatory = (Boolean) field.get("is_mandatory");
		Boolean isVisible = (Boolean) field.get("is_visible");

		if (columnName.length() == 0) {
			throw new CustomException("column_name value cannot be empty.");
		}

		isTableExists(table, model);

		isColumnExists(table, columnName, model);

		if (displayName.length() == 0)
			throw new CustomException("display_name value cannot be empty.");

		if (!Stream.of("text", "date_time", "numeric").anyMatch(columnType::equalsIgnoreCase))
			throw new CustomException("column_type value: " + columnType
					+ "  is not appropriate. Please use one of the following values ['text','date_time','numeric']");

		if (!Stream.of("checkbox", "textbox", "dropdownlist", "calendar").anyMatch(controlType::equalsIgnoreCase))
			throw new CustomException("control_type value: " + controlType
					+ "  is not appropriate. Please use one of the following values ['checkbox','textbox','dropdownlist','calendar']");

		if (isMandatory != true && isMandatory != false)
			throw new CustomException("is_mandatory is a boolean value. Please use true/false");

		if (isVisible != true && isVisible != false)
			throw new CustomException("is_visible is a boolean value. Please use true/false");

		return true;
	}

	private boolean isColumnExists(String table, String columnName, Model model) {

		ModelDatasetSkeletonTables modelDatasetSkeletonTables = modelSkeletonTablesRepository
				.findByModelIdAndTableNameAndStatus(model.getId(), table, true);

		Boolean isFieldsExists = modelSkeletonFieldsRepository
				.existsByTableIdAndColumnName(modelDatasetSkeletonTables.getId(), columnName);

		if (isFieldsExists)
			throw new CustomException(
					"Column: " + columnName + " is already exists in the skeleton for table: " + table);

		return true;
	}

	private boolean isTableExists(String table, Model model) {

		Boolean isModelSkeletonExistsTable = modelSkeletonTablesRepository
				.existsByModelIdAndTableNameAndStatus(model.getId(), table, true);

		if (!isModelSkeletonExistsTable)
			throw new CustomException("table value: " + table
					+ " is not appropriate. Please use one of the following values [timewindows, resources,resources_timewindows,services"
					+ " skills,tasks,tasks_timewindows,location,resource_location,task_location");

		return true;
	}

	@Override
	public Map<String, List<Map<String, Object>>> getSkeletonByDsId(Integer datasetId) {

		List<Object[]> skeletonList = modelSkeletonTablesRepository.findSkeletonByDsId(datasetId);

		if (skeletonList == null)
			throw new CustomException("No Data Found");

		Map<String, List<Map<String, Object>>> skeletonRes = new HashMap<>();

		List<Map<String, Object>> skeleton = new ArrayList<>();

		for (Object[] row : skeletonList) {

			Map<String, Object> rowMap = new HashMap<>();

			rowMap.put("id", row[0]);
			rowMap.put("model_id", row[1]);
			rowMap.put("dataset_id", row[2]);
			rowMap.put("table_name", row[3]);
			rowMap.put("table_display_name", row[4]);
			rowMap.put("columnName", row[5]);
			rowMap.put("fieldSeqNo", row[6]);
			rowMap.put("displayName", row[7]);
			rowMap.put("column_type", row[8]);
			rowMap.put("control_type", row[9]);
			rowMap.put("column_format", row[10]);
			rowMap.put("is_editable", Boolean.valueOf(row[11].toString()) == true ? 1 : 0);
			rowMap.put("is_mandatory", Boolean.valueOf(row[12].toString()) == true ? 1 : 0);
			rowMap.put("is_visible", Boolean.valueOf(row[13].toString()) == true ? 1 : 0);
			rowMap.put("status", Boolean.valueOf(row[14].toString()) == true ? 1 : 0);
			rowMap.put("created_by", row[15]);
			rowMap.put("updated_by", row[16]);
			rowMap.put("createdAt", row[17]);
			rowMap.put("updatedAt", row[18]);

			skeleton.add(rowMap);
		}

		skeletonRes.put("ds_skl", skeleton);
		return skeletonRes;

	}
}
