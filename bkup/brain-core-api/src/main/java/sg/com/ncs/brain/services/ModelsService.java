package sg.com.ncs.brain.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import sg.com.ncs.brain.entities.model.Model;
import sg.com.ncs.brain.entities.model.ModelAccess;

public interface ModelsService {

	List<Map<String, Object>> fetchModels();

	Model addModel(Map<?, ?> req);

	ModelAccess saveModelAccess(Map<?, ?> req);

	public Optional<Model> fetchTest();

	void updateModelStatus(Map<?, ?> req);
}
