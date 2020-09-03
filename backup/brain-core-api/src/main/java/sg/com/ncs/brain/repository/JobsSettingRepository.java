package sg.com.ncs.brain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.com.ncs.brain.entities.model.JobsSettings;

public interface JobsSettingRepository extends JpaRepository<JobsSettings, Integer> {

}
