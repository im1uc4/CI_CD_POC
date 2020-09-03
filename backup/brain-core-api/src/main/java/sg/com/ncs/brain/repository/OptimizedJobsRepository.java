package sg.com.ncs.brain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.com.ncs.brain.entities.model.OptimizedJobs;

public interface OptimizedJobsRepository extends JpaRepository<OptimizedJobs, Integer> {

	List<OptimizedJobs> deleteByJobId(Integer id);

	List<OptimizedJobs> findByTaskId(String string);

	Integer countByTaskId(String string);

	void deleteByTaskId(String taskId);

	List<OptimizedJobs> findByJobId(Integer id);
}
