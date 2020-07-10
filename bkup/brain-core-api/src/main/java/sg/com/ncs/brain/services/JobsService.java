package sg.com.ncs.brain.services;

import java.util.List;
import java.util.Map;

import sg.com.ncs.brain.entities.model.JobsStatusUpdate;

public interface JobsService {

	List<JobsStatusUpdate> fetchJobStatus(Long id);

	Object addJobsCallBack(Map<?, ?> req);

	Map<String, List<Map<String, Object>>> getJobsForModel(int parseInt);

	List<Map<String, List<Map<String, Object>>>> fetchOptimizedJobs(String jobId);

	List<Integer> updateOptimizedJobs(Map<?, ?> req);

	List<Integer> deleteOptimizedJobs(Map<?, ?> req);

	List<Map<String, List<Map<String, Object>>>> getJobsStatus(String jobId);

}
