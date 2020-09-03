package sg.com.ncs.brain.services;

import java.util.List;
import java.util.Map;

import sg.com.ncs.brain.entities.model.GeneralFieldsTemplates;

public interface SkeletonService {

	GeneralFieldsTemplates addSkeleton(Map<?, ?> req);

	public Map<String, List<Map<String, Object>>> getSkeletonByDsId(Integer datasetId);
}
