package sg.com.ncs.brain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.com.ncs.brain.entities.model.JobsStatusUpdate;

public interface JobsStatusRepository extends JpaRepository<JobsStatusUpdate, Integer> {

	JobsStatusUpdate findByJobId(Integer id);
}
