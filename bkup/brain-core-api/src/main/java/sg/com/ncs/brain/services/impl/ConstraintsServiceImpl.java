package sg.com.ncs.brain.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.com.ncs.brain.entities.model.ConstraintParam;
import sg.com.ncs.brain.entities.model.ConstraintParamStructure;
import sg.com.ncs.brain.entities.model.ConstraintType;
import sg.com.ncs.brain.entities.model.Constraints;
import sg.com.ncs.brain.repository.ConstraintParamRepository;
import sg.com.ncs.brain.repository.ConstraintParamStructureRepository;
import sg.com.ncs.brain.repository.ConstraintTypeRepository;
import sg.com.ncs.brain.repository.ConstraintsRepository;
import sg.com.ncs.brain.services.ConstraintsService;
import sg.com.ncs.common.exceptions.CustomException;

@Service
public class ConstraintsServiceImpl implements ConstraintsService {

	@Autowired
	private ConstraintsRepository constraintsRepository;

	@Autowired
	private ConstraintParamRepository constraintParamRepository;

	@Autowired
	private ConstraintParamStructureRepository constraintParamStructureRepository;

	@Autowired
	private ConstraintTypeRepository constraintTypeRepository;

	public Map<String, List<Map<String, Object>>> fetchConstraints(Integer model_id) {
		List<Constraints> constraints = constraintsRepository.findAllByModelId(model_id);

		List<Map<String, Object>> constraintsList = new ArrayList<>();
		constraints.forEach(constraint -> {
			Map<String, Object> constraintMap = new HashMap<>();

			constraintMap.put("id", constraint.getId());
			constraintMap.put("model_id", constraint.getModel().getId());
			constraintMap.put("constraintName", constraint.getDisplayName());
			constraintMap.put("constraintNum", constraint.getConstraintNum());
			constraintMap.put("constraint_type_id", constraint.getConstraintType().getId());
			constraintMap.put("scoreLevel", constraint.getScoreLevel());
			constraintMap.put("scoreLeveldescription", constraint.getScoreLevel());
			constraintMap.put("scoreWeight", constraint.getScoreWeight());
			constraintMap.put("status", constraint.getStatus());
			constraintMap.put("created_by", constraint.getCreatedBy());
			constraintMap.put("updated_by", constraint.getUpdatedBy());
			constraintMap.put("createdAt", constraint.getCreatedAt());
			constraintMap.put("updatedAt", constraint.getUpdatedAt());

			constraintsList.add(constraintMap);
		});
		if (!(constraintsList.size() > 0))
			throw new CustomException("Constraints not found for specified Model id.");

		Map<String, List<Map<String, Object>>> response = new HashMap<>();
		response.put("constraints", constraintsList);

		return response;
	}

	public List<Constraints> fetchAllConstraints() {
		List<Constraints> constraints = constraintsRepository.findAll();
		return constraints;
	}

	@SuppressWarnings("unchecked")
	public List<ConstraintType> saveConstraints(Map<?, ?> req) {

		if (!req.containsKey("constraintTypes"))
			throw new CustomException("No constraintTypes key provided in the body.");

		List<ConstraintType> constraintTypes = saveJsonConstraintTypes((List<Map<?, ?>>) req.get("constraintTypes"));

		return constraintTypes;
	}

	@SuppressWarnings("unchecked")
	private List<ConstraintType> saveJsonConstraintTypes(List<Map<?, ?>> constraintTypes) {

		constraintTypes.forEach(constraintType -> {

			String constraintTypeId = constraintType.get("constraintTypeId").toString();
			String constraintTypeName = constraintType.get("constraintTypeName").toString();
			String defaultDisplayName = constraintType.get("defaultDisplayName").toString();

			ConstraintType constraintTypeDB = insertConstraintTypes(constraintTypeId, constraintTypeName,
					defaultDisplayName);

			insConstraintTypeParam(constraintTypeDB, (List<Map<?, ?>>) constraintType.get("parameterList"));

		});

		return null;
	}

	private void insConstraintTypeParam(ConstraintType constraintTypeDB, List<Map<?, ?>> parameterList) {
		parameterList.forEach(parameter -> {

			String parameterType = parameter.get("parameterType").toString();
			String parameterId = parameter.get("parameterId").toString();
			String parameterNum = parameter.get("parameterNum").toString();
			String name = parameter.get("name").toString();

			ConstraintParam constraintParam = new ConstraintParam();

			constraintParam.setConstraintType(constraintTypeDB);
			constraintParam.setParameterId(parameterId);
			constraintParam.setParameterNum(parameterNum);
			constraintParam.setName(name);
			constraintParam.setParameterType(parameterType);

			ConstraintParam constraintParamDB = null;

			if (!constraintParamRepository.existsByParameterIdAndConstraintTypeId(parameterId,
					constraintTypeDB.getId())) {

				constraintParamDB = constraintParamRepository.save(constraintParam);
			} else
				constraintParamDB = constraintParamRepository.findByParameterIdAndConstraintTypeId(parameterId,
						constraintTypeDB.getId());

			final ConstraintParam constraintParamDBFinal = constraintParamDB;

			AtomicInteger counter = new AtomicInteger();

			parameter.keySet().forEach(parameterKey -> {

				ConstraintParamStructure constraintParamStructure = new ConstraintParamStructure();

				String value = null;
				if (!(parameter.get(parameterKey) == null))
					value = parameter.get(parameterKey).toString();
				constraintParamStructure.setValue(value);
				constraintParamStructure.setConstraintParam(constraintParamDBFinal);
				constraintParamStructure.setKey(parameterKey + "");
				constraintParamStructure.setSeqNo(counter.incrementAndGet());

				if (!constraintParamStructureRepository.existsByConstraintParamIdAndKey(constraintParamDBFinal.getId(),
						parameterKey)) {
					constraintParamStructureRepository.save(constraintParamStructure);
				}
			});
		});
	}

	private ConstraintType insertConstraintTypes(String constraintTypeId, String constraintTypeName,
			String defaultDisplayName) {

		if (!constraintTypeRepository.existsByConstraintTypeId(constraintTypeId)) {
			ConstraintType constraintType = new ConstraintType();
			constraintType.setDefaultDisplayName(defaultDisplayName);
			constraintType.setConstraintTypeId(constraintTypeId);
			constraintType.setConstraintTypeName(constraintTypeName);
			return constraintTypeRepository.save(constraintType);
		} else
			return constraintTypeRepository.findByConstraintTypeId(constraintTypeId);
	}

}
