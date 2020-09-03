package sg.com.ncs.brain.services;

import java.util.List;
import java.util.Map;

import sg.com.ncs.brain.model.dataset.Datasets;

public interface DatasetService {

	Datasets addDatasets(Map<?, ?> req);

	Map<?, Object> updateDatasets(Map<?, ?> datasets);

	public Map<String, List<Map<String, Object>>> getDsForModel(Integer modelId);

	public Map<String, List<Map<String, Object>>> getRawDataById(Integer datasetId);
}
