package sg.com.ncs.brain.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import sg.com.ncs.brain.entities.model.AccessType;
import sg.com.ncs.brain.entities.model.ConstraintParam;
import sg.com.ncs.brain.entities.model.ConstraintParamStructure;
import sg.com.ncs.brain.entities.model.ConstraintParamValue;
import sg.com.ncs.brain.entities.model.ConstraintType;
import sg.com.ncs.brain.entities.model.Constraints;
import sg.com.ncs.brain.entities.model.GeneralFieldsTemplates;
import sg.com.ncs.brain.entities.model.GeneralTablesTemplates;
import sg.com.ncs.brain.entities.model.GroupModelAccess;
import sg.com.ncs.brain.entities.model.Model;
import sg.com.ncs.brain.entities.model.ModelAccess;
import sg.com.ncs.brain.entities.model.ModelCustomFields;
import sg.com.ncs.brain.entities.model.ModelDatasetSkeletonFields;
import sg.com.ncs.brain.entities.model.ModelDatasetSkeletonTables;
import sg.com.ncs.brain.entities.model.UserModelAccess;
import sg.com.ncs.brain.entities.user.maint.Groups;
import sg.com.ncs.brain.entities.user.maint.User;
import sg.com.ncs.brain.repository.AccessTypeRepository;
import sg.com.ncs.brain.repository.ConstraintParamRepository;
import sg.com.ncs.brain.repository.ConstraintParamStructureRepository;
import sg.com.ncs.brain.repository.ConstraintTypeRepository;
import sg.com.ncs.brain.repository.ConstraintsParamValueRepository;
import sg.com.ncs.brain.repository.ConstraintsRepository;
import sg.com.ncs.brain.repository.GeneralFieldsTemplatesRepository;
import sg.com.ncs.brain.repository.GeneralTableTemplatesRepository;
import sg.com.ncs.brain.repository.GroupModelAccessRepository;
import sg.com.ncs.brain.repository.GroupsRepository;
import sg.com.ncs.brain.repository.ModelAccessRepository;
import sg.com.ncs.brain.repository.ModelCustomFieldsRepository;
import sg.com.ncs.brain.repository.ModelRepository;
import sg.com.ncs.brain.repository.ModelSkeletonFieldsRepository;
import sg.com.ncs.brain.repository.ModelSkeletonTablesRepository;
import sg.com.ncs.brain.repository.UserGroupsRepository;
import sg.com.ncs.brain.repository.UserModelAccessRepository;
import sg.com.ncs.brain.repository.UserRepository;
import sg.com.ncs.brain.services.ModelsService;
import sg.com.ncs.common.exceptions.CustomException;
import sg.com.ncs.common.exceptions.DataException;

@Service
public class ModelsServiceImpl implements ModelsService {

	@Autowired
	ModelRepository modelRepository;

	@Autowired
	private AuditorAware auditAware;

	@Autowired
	UserModelAccessRepository userModelAccessRepository;

	@Autowired
	GroupModelAccessRepository groupModelAccessRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserGroupsRepository userGroupsRepository;

	@Autowired
	AccessTypeRepository accessTypeRepository;

	@Autowired
	GeneralFieldsTemplatesRepository generalFieldsTemplatesRepository;

	@Autowired
	GeneralTableTemplatesRepository generalTableTemplatesRepository;

	@Autowired
	ModelSkeletonFieldsRepository modelSkeletonFieldsRepository;

	@Autowired
	ModelSkeletonTablesRepository modelSkeletonTablesRepository;

	/*
	 * @Autowired RolesRepository rolesRepository;
	 */
	@Autowired
	ConstraintTypeRepository constraintTypeRepository;

	@Autowired
	ConstraintParamRepository constraintParamRepository;

	@Autowired
	ConstraintsParamValueRepository constraintsParamValueRepository;

	@Autowired
	ConstraintParamStructureRepository constraintParamStructureRepository;

	@Autowired
	ModelCustomFieldsRepository modelCustomFieldsRepository;

	@Autowired
	ConstraintsRepository constraintsRepository;

	@Autowired
	private ModelAccessRepository modelAccessRepository;

	@Autowired
	private GroupsRepository groupsRepository;

	@Bean
	PasswordEncoder getEncoder() {
		return new BCryptPasswordEncoder();
	}

	public Optional<Model> fetchTest() {
		Optional<Model> model = modelRepository.findById(1);
	//	System.out.println("sis");
		return model;
	}

	public List<Map<String, Object>> fetchModels() {

		String username = auditAware.getCurrentAuditor().get().toString();

		List<GroupModelAccess> groupModelAccessList = groupModelAccessRepository.findAllByUsername(username);
		List<UserModelAccess> userModelAccessList = userModelAccessRepository.findAllByUsername(username);

		List<Map<String, Object>> response = new ArrayList<>();

		groupModelAccessList.forEach(modelAccess -> {
			Map<String, Object> modelAccessMap = new HashMap<>();

			modelAccessMap.put("created_by", modelAccess.getCreatedBy());
			modelAccessMap.put("id", modelAccess.getId());
			modelAccessMap.put("model_description_details", modelAccess.getModelDescriptionDetails());
			modelAccessMap.put("model_description_header", modelAccess.getModelDescriptionHeader());
			modelAccessMap.put("model_name", modelAccess.getModelName());
			modelAccessMap.put("person_in_charge", modelAccess.getPersonInCharge());
			response.add(modelAccessMap);
		});

		userModelAccessList.forEach(modelAccess -> {
			Map<String, Object> modelAccessMap = new HashMap<>();

			modelAccessMap.put("created_by", modelAccess.getCreatedBy());
			modelAccessMap.put("id", modelAccess.getId());
			modelAccessMap.put("model_description_details", modelAccess.getModelDescriptionDetails());
			modelAccessMap.put("model_description_header", modelAccess.getModelDescriptionHeader());
			modelAccessMap.put("model_name", modelAccess.getModelName());
			modelAccessMap.put("person_in_charge", modelAccess.getPersonInCharge());
			response.add(modelAccessMap);
		});

		return response;
	}

	@Override
	public ModelAccess saveModelAccess(Map<?, ?> req) {

		if (!req.containsKey("modelId"))
			throw new CustomException("modelId key is missing in the JSON body.");

		if (!req.containsKey("access_type"))
			throw new CustomException("skeletonField key is missing in the JSON body.");

		if (!req.containsKey("data"))
			throw new CustomException("data key is missing in the JSON body.");

		String optModelId = req.get("modelId").toString();
		String accessType = req.get("access_type").toString();
		List<String> accessdata = (List<String>) req.get("data");

		Boolean isModelExists = isModelExistsByModelIdAndStatus(optModelId, true);

		if (!isModelExists)
			throw new CustomException(
					"Optimization Model " + optModelId + " is missing in the DB. To proceed, please import model.");

		Model modelDb = getModlId(optModelId);

		AccessType accessTypeDb = accessTypeRepository.findByAccessTypeAndStatus(accessType, true);

		if (accessTypeDb == null)
			throw new CustomException(
					"access_type value is invalid. Please make sure it's included into `access_types` table.");

		for (String username : accessdata) {

			Integer targetId = null;

			if (accessType.equals("user")) {
				User user = checkUserExists(username);

				targetId = user.getId();
			}
			if (accessType.equals("group")) {
				Groups group = checkGroupExists(username);
				targetId = group.getId();
			}

			ModelAccess modelAccessDb = modelAccessRepository.findByModelIdAndAccessTypeIdAndTargetId(modelDb.getId(),
					accessTypeDb.getId(), targetId);

			if (modelAccessDb == null) {
				ModelAccess modelAccess = new ModelAccess();
				modelAccess.setAccessType(accessTypeDb);
				modelAccess.setModel(modelDb);
				modelAccess.setTargetId(targetId);
				modelAccess.setStatus(true);
				modelAccessDb = modelAccessRepository.save(modelAccess);
			}

			return modelAccessDb;
		}
		return null;
	}

	private Groups checkGroupExists(String username) {

		Groups group = groupsRepository.findBynameAndStatus(username, true);

		if (group == null)
			throw new CustomException("group " + username + " not exists in the DB.");

		return group;
	}

	private User checkUserExists(String username) {
		User user = userRepository.findByUsernameAndStatus(username, true);

		if (user == null)
			throw new CustomException("User " + username + " not exists in the DB.");

		return user;
	}

	private Model getModlId(String optModelId) {

		Model model = modelRepository.findByOptimisationModelIdAndModelStatus(optModelId, true);

		if (model == null)
			throw new CustomException("Model not found, check with tech team");

		return model;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Model addModel(Map<?, ?> req) {

		// throw new CustomException("Data Exception");

		validateMainComponent(req, "Header");

		String optimisationModelId = req.get("optimisationModelId").toString();
		Integer versionNum = (Integer) req.get("versionNum");
		String modelName = req.get("name").toString();
		String titleDescription = req.get("titleDescription").toString();
		String description = req.get("description").toString();
		Map<?, ?> customFieldName = (Map<?, ?>) req.get("customFieldName");

		String grantForUser = null;

		if (req.containsKey("grantforUser"))
			grantForUser = req.get("grantforUser").toString();

		Model model = new Model();

		model.setOptimisationModelId(optimisationModelId);
		model.setVersionNum(versionNum);
		model.setModelName(modelName);
		model.setModelDescriptionHeader(titleDescription);
		model.setModelDescriptionDetails(description);
		model.setModelStatus(true);

		Model savedModel = modelRepository.save(model);

		List<ModelCustomFields> modelCustomFieldsList = insertCustomFields(customFieldName, savedModel);
		try {
			insertConst((List<Map<?, ?>>) req.get("constraintList"), savedModel, req);
		} catch (Exception e) {

			if (savedModel.getId() > 0) {

				deleteModel(savedModel, e);
			}
			throw new DataException(e);
		}

		try {
			insertDataSkeleton(savedModel);
		} catch (Exception e) {
			if (savedModel.getId() > 0) {
				deleteModel(savedModel, e);
			}
		}
		if (grantForUser != null)
			assignUser(savedModel, grantForUser);

		return savedModel;
	}

	private void deleteModel(Model savedModel, Exception e) {
		modelRepository.delete(savedModel);

		if (e.getClass() == CustomException.class)
			throw new DataException(e.getMessage());
		else
			throw new DataException(e);
	}

	private Boolean validateMainComponent(Map<?, ?> req, String type) {

		if (type.equals("Header")) {

			if (!req.containsKey("optimisationModelId"))
				throw new CustomException("Model optimisationModelId is missing.");

			Boolean modelExists = isModelExistsByModelIdAndStatus(req.get("optimisationModelId").toString(), true);

			if (modelExists)
				throw new CustomException("Model with optimisationModelId already exists in the DB.");

			if (!req.containsKey("versionNum"))
				throw new CustomException("Model versionNum is missing.");

			if (!req.containsKey("name"))
				throw new CustomException("Model name is missing.");

			if (!req.containsKey("titleDescription"))
				throw new CustomException("Model titleDescription is missing.");

			if (!req.containsKey("description"))
				throw new CustomException("Model description is missing.");

			if (!req.containsKey("constraintList"))
				throw new CustomException("Model constraintList is missing.");
		}
		if (type.equals("constraintList")) {

			if (!req.containsKey("constraintId"))
				throw new CustomException("constraintList/constraintId is missing.");

			if (!req.containsKey("constraintNum"))
				throw new CustomException("constraintList/constraintNum is missing.");

			if (!req.containsKey("displayName"))
				throw new CustomException("constraintList/displayName is missing.");

			if (!req.containsKey("scoreLevel"))
				throw new CustomException("constraintList/scoreLevel is missing.");

			if (!req.containsKey("scoreWeight"))
				throw new CustomException("constraintList/scoreWeight is missing.");
		}

		return true;
	}

	private Boolean isModelExistsByModelIdAndStatus(String optimisationModelId, Boolean status) {

		Boolean modelExists = modelRepository.existsByOptimisationModelIdAndModelStatus(optimisationModelId, status);

		return modelExists;
	}

	private void assignUser(Model savedModel, String grantForUser) {

		Boolean userExists = userRepository.existsByUsername(grantForUser);

		if (!userExists) {

			User user = new User();

			user.setUsername(grantForUser);

			user.setFullname(grantForUser);
			user.setDutyTitle("");
			user.setEmail("");
			user.setStatus(true);
			user.setPassword(getEncoder().encode(grantForUser));

			User userDb = userRepository.save(user);

			if (userDb.getId() > 0) {

				AccessType accessType = accessTypeRepository.findByAccessTypeAndStatus("user", true);

				if (accessType == null)
					throw new CustomException("Access Type for User not found in Database, Please Import First");

				Boolean isModelAccessExists = modelAccessRepository.existsByModelIdAndAccessTypeIdAndTargetId(
						savedModel.getId(), accessType.getId(), userDb.getId());

				if (!isModelAccessExists) {
					ModelAccess modelAccess = new ModelAccess();
					modelAccess.setModel(savedModel);
					modelAccess.setAccessType(accessType);
					modelAccess.setStatus(true);
					modelAccess.setTargetId(userDb.getId());
					modelAccessRepository.save(modelAccess);
				}
			}
		}
	}

	private void insertDataSkeleton(Model savedModel) {

		List<GeneralTablesTemplates> generalTablesTemplates = generalTableTemplatesRepository.findAllByStatus(true);

		for (GeneralTablesTemplates tableTemplate : generalTablesTemplates) {

			ModelDatasetSkeletonTables modelDatasetSkeletonTablesDb = modelSkeletonTablesRepository
					.findByModelIdAndTableNameAndStatus(savedModel.getId(), tableTemplate.getTableName(), true);

			ModelDatasetSkeletonTables modelDatasetSkeletonTables = new ModelDatasetSkeletonTables();

			modelDatasetSkeletonTables.setDisplayName(tableTemplate.getDisplayName());
			modelDatasetSkeletonTables.setIsEditable(tableTemplate.getIsEditable());
			modelDatasetSkeletonTables.setModel(savedModel);
			modelDatasetSkeletonTables.setStatus(tableTemplate.getStatus());
			modelDatasetSkeletonTables.setTableName(tableTemplate.getTableName());
			modelDatasetSkeletonTables.setTableSeqNo(tableTemplate.getTableSeqNo());

			if (modelDatasetSkeletonTablesDb == null)
				modelDatasetSkeletonTablesDb = modelSkeletonTablesRepository.save(modelDatasetSkeletonTables);

			if (modelDatasetSkeletonTablesDb != null) {

				List<GeneralFieldsTemplates> generalFieldsTemplates = generalFieldsTemplatesRepository
						.findAllByTableIdAndStatus(tableTemplate.getId(),true);

				List<ModelDatasetSkeletonFields> modelDatasetSkeletonFieldsList = new ArrayList<>();

				if (modelDatasetSkeletonTablesDb != null) {

					for (GeneralFieldsTemplates fieldTemplate : generalFieldsTemplates) {
						ModelDatasetSkeletonFields modelDatasetSkeletonFields = new ModelDatasetSkeletonFields();
						modelDatasetSkeletonFields.setTable(modelDatasetSkeletonTablesDb);
						modelDatasetSkeletonFields.setColumnName(fieldTemplate.getColumnName());
						modelDatasetSkeletonFields.setColumnType(fieldTemplate.getColumnType());
						modelDatasetSkeletonFields.setFieldSeqNo(fieldTemplate.getFieldSeqNo());
						modelDatasetSkeletonFields.setDisplayName(fieldTemplate.getDisplayName());
						modelDatasetSkeletonFields.setColumnFormat(fieldTemplate.getColumnFormat());
						modelDatasetSkeletonFields.setControlType(fieldTemplate.getControlType());
						modelDatasetSkeletonFields.setIsDynamic(fieldTemplate.getIsDynamic());
						modelDatasetSkeletonFields.setIsMandatory(fieldTemplate.getIsMandatory());
						modelDatasetSkeletonFields.setIsVisible(fieldTemplate.getIsVisible());
						modelDatasetSkeletonFields.setStatus(fieldTemplate.getStatus());

						Boolean fieldExist = modelSkeletonFieldsRepository.existsByTableIdAndColumnName(
								modelDatasetSkeletonTablesDb.getId(), fieldTemplate.getColumnName());

						if (!fieldExist)
							modelDatasetSkeletonFieldsList.add(modelDatasetSkeletonFields);
					}

					modelSkeletonFieldsRepository.saveAll(modelDatasetSkeletonFieldsList);

				}

			}

		}

	}

	private List<ModelCustomFields> insertCustomFields(Map<?, ?> customFieldName, Model savedModel) {
		if (customFieldName != null) {

			List<ModelCustomFields> modelCustomFieldsList = new ArrayList<>();

			customFieldName.keySet().forEach(field -> {

				ModelCustomFields modelCustomFields = new ModelCustomFields();

				modelCustomFields.setCustomFieldName(field.toString());
				modelCustomFields.setCustomFieldValue(customFieldName.get(field).toString());
				modelCustomFields.setModel(savedModel);
				modelCustomFields.setStatus(true);

				modelCustomFieldsList.add(modelCustomFields);

			});

			savedModel.setModelCustomFieldList(modelCustomFieldsList);
			return modelCustomFieldsRepository.saveAll(modelCustomFieldsList);

		}
		return null;

	}

	private void insertConst(List<Map<?, ?>> constraintList, Model savedModel, Map<?, ?> req) {

		constraintList.forEach(constraint -> {

			validateMainComponent(constraint, "constraintList");

			String constraintId = constraint.get("constraintId").toString();
			Integer constraintNum = (Integer) constraint.get("constraintNum");
			String displayName = constraint.get("displayName").toString();
			String scoreLevel = constraint.get("scoreLevel").toString();
			String scoreWeight = constraint.get("scoreWeight").toString();

			ConstraintType constraintType = getIdByConstraintTypeId((Map<?, ?>) constraint.get("constraintType"));

			Constraints constraints = new Constraints();
			constraints.setConstraintId(constraintId);
			constraints.setConstraintNum(constraintNum);
			constraints.setConstraintType(constraintType);
			constraints.setDisplayName(displayName);
			constraints.setScoreLevel(scoreLevel);
			constraints.setScoreWeight(scoreWeight);
			constraints.setModel(savedModel);

			Constraints savedConstraints = insertConstraint(constraints);

			insertConstraintType((Map<?, ?>) constraint.get("constraintType"), savedConstraints);

			List<Constraints> constraintsList = new ArrayList<>();
			constraintsList.add(savedConstraints);
			savedModel.setConstraintList(constraintsList);

		});

	}

	@SuppressWarnings("unchecked")
	private void insertConstraintType(Map<?, ?> constraintType, Constraints savedConstraints) {
		String constraintTypeId = constraintType.get("constraintTypeId").toString();
		ConstraintType constraintTypeDB = constraintTypeRepository.findByConstraintTypeId(constraintTypeId);

		if (constraintTypeDB == null) {
			throw new CustomException(
					"Constraint Id: " + constraintTypeId + " has not been found in the system. Please import it first");
		}

		if (constraintTypeDB != null && constraintType.containsKey("parameterList")) {

			List<Map<?, ?>> objectCnParameters = (List<Map<?, ?>>) constraintType.get("parameterList");

			objectCnParameters.forEach(param -> {

				String parameterId = param.get("parameterId").toString();

				ConstraintParam constParam = constraintParamRepository.findByParameterIdAndConstraintTypeId(parameterId,
						constraintTypeDB.getId());

				if (constParam == null) {
					throw new CustomException("Contraint Parameter " + parameterId + " not found");
				} else {

					List<ConstraintParamStructure> constraintParamStructure = constraintParamStructureRepository
							.findByConstraintParamId(constParam.getId());

					List<ConstraintParamValue> constraintParamValueList = new ArrayList<>();

					constraintParamStructure.forEach(constraintParam -> {

						ConstraintParamValue constraintParamValue = new ConstraintParamValue();
						String key = constraintParam.getKey();
						String value = constraintParam.getValue();

						if (value == null) {
							if (param.containsKey(key)) {

								value = param.get(key).toString();
							} else
								return;
						}
						constraintParamValue.setValue(value);
						constraintParamValue.setConstraint(savedConstraints);
						constraintParamValue.setConstraintParam(constParam);
						constraintParamValue.setConstraintParamKey(constraintParam);

						constraintParamValueList.add(constraintParamValue);

					});

					constraintsParamValueRepository.saveAll(constraintParamValueList);
					savedConstraints.setParamList(constraintParamValueList);
				}

			});
		}

	}

	private Constraints insertConstraint(Constraints constraints) {
		return constraintsRepository.save(constraints);
	}

	private ConstraintType getIdByConstraintTypeId(Map<?, ?> constraintType) {

		String constraintTypeId = constraintType.get("constraintTypeId").toString();

		ConstraintType constraintTypeDB = constraintTypeRepository.findByConstraintTypeId(constraintTypeId);

		if (constraintTypeDB == null)
			throw new CustomException(
					"Constraint Id: " + constraintTypeId + " has not been found in the system. Please import it first");

		return constraintTypeDB;
	}

	@Override
	public void updateModelStatus(Map<?, ?> req) {

		checkJSONMainComponent(req, "Header");

		String setOptimisationModelStatus = req.get("setOptimisationModelStatus").toString();
		String optimisationModelId = req.get("optimisationModelId").toString();

		if ((setOptimisationModelStatus != "true") && (setOptimisationModelStatus != "false")) {

			throw new CustomException("setOptimisationModelStatus can accept only true/false values.");
		}

		Model modelDb = modelRepository.findByOptimisationModelIdAndModelStatus(optimisationModelId,
				!Boolean.getBoolean(setOptimisationModelStatus));

		if (modelDb != null) {

			modelDb.setModelStatus(Boolean.getBoolean(setOptimisationModelStatus));

		} else {

			throw new CustomException("No update performed. Current status of the model is "
					+ (setOptimisationModelStatus.equals("true") ? "Active" : "Inactive"));

		}
	}

	private void checkJSONMainComponent(Map<?, ?> req, String type) {

		if (!req.containsKey("optimisationModelId"))
			throw new CustomException("Model optimisationModelId is missing.");

		isModelAbsent(req.get("optimisationModelId").toString());

		if (!req.containsKey("setOptimisationModelStatus"))
			throw new CustomException("Model versionNum is missing.");
	}

	private Boolean isModelAbsent(String optimisationModelId) {

		Boolean modelExists = modelRepository.existsByOptimisationModelId(optimisationModelId);

		if (!modelExists)
			throw new CustomException("Model with optimisationModelId dosen't exists in the DB.");

		return false;
	}

}
