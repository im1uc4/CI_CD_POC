package sg.com.ncs.brain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.com.ncs.brain.entities.model.Jobs;
import sg.com.ncs.brain.entities.model.JobsStatusUpdate;

public interface JobsRepository extends JpaRepository<Jobs, Integer> {

	List<Jobs> findAllByModelId(Long optimisationModelId);

	List<JobsStatusUpdate> findAllByJobId(Long jobId);

	Jobs findByJobId(String jobId);

	Jobs findByJobIdAndStatus(String jobId, boolean b);

	@Query(value = "SELECT \r\n" + "    jb.id 'id',\r\n" + "    jb.model_id 'model_id',\r\n"
			+ "    jb.dataset_id 'dataset_id',\r\n" + "    jb.jobId 'job_id',\r\n"
			+ "    jb.description 'job_description',\r\n" + "    ds.name 'dataset_name',\r\n"
			+ "    ds.description 'dataset_description',\r\n"
			+ "    ds.scheduleDateTime 'dataset_scheduledatetime',\r\n"
			+ "    jb.completed_datetime 'completed_datetime',\r\n"
			+ "    jb.submitted_datetime 'submitted_datetime',\r\n" + "    usr.name 'created_by',\r\n"
			+ "    jb.updated_by 'updated_by',\r\n" + "    jb.createdAt 'createdAt',\r\n"
			+ "    jb.updatedAt 'updatedAt'\r\n" + "FROM\r\n" + "    jobs jb\r\n" + "        INNER JOIN\r\n"
			+ "    datasets ds ON jb.dataset_id = ds.id\r\n" + "        INNER JOIN\r\n"
			+ "    models m ON m.id = ds.model_id\r\n" + "        INNER JOIN\r\n"
			+ "    jobs_statuses js ON jb.id = js.job_id\r\n" + "        INNER JOIN\r\n"
			+ "    users usr ON jb.created_by = usr.username\r\n" + "WHERE\r\n" + "    jb.model_id = :modelId\r\n"
			+ "        AND js.job_status = 'COMPLETED'", nativeQuery = true)
	List<Object[]> findByJobsModelId(@Param("modelId") Integer modelId);

	@Query(value = "SELECT \r\n" + "    `jobs`.`id`,\r\n" + "    jobs.jobId AS 'jobId',\r\n"
			+ "    `optimized_jobs`.`id` AS `optimized_jobs.id`,\r\n"
			+ "    `optimized_jobs`.`job_id` AS `optimized_jobs.job_id`,\r\n"
			+ "    `optimized_jobs`.`taskid` AS `optimized_jobs.taskId`,\r\n"
			+ "    `optimized_jobs`.`taskName` AS `optimized_jobs.taskName`,\r\n"
			+ "    `optimized_jobs`.`startDateTime` AS `optimized_jobs.startDateTime`,\r\n"
			+ "    `optimized_jobs`.`endDateTime` AS `optimized_jobs.endDateTime`,\r\n"
			+ "    `optimized_jobs`.`taskDuration` AS `optimized_jobs.taskDuration`,\r\n"
			+ "    `optimized_jobs`.`resourceLocked` AS `optimized_jobs.resourceLocked`,\r\n"
			+ "    `optimized_jobs`.`startDateTimeLocked` AS `optimized_jobs.startDateTimeLocked`,\r\n"
			+ "    `optimized_jobs`.`customFieldList` AS `optimized_jobs.customFieldList`,\r\n"
			+ "    `optimized_jobs`.`serviceId` AS `optimized_jobs.serviceId`,\r\n"
			+ "    `optimized_jobs`.`timeWindowId` AS `optimized_jobs.timeWindowId`,\r\n"
			+ "    `optimized_jobs`.`resourceId` AS `optimized_jobs.resourceId`,\r\n"
			+ "    `optimized_jobs`.`location` AS `optimized_jobs.location`\r\n" + "FROM\r\n"
			+ "    `jobs` AS `jobs`\r\n" + "        INNER JOIN\r\n"
			+ "    `optimized_jobs` AS `optimized_jobs` ON `jobs`.`id` = `optimized_jobs`.`job_id`\r\n" + "WHERE\r\n"
			+ "    `jobs`.`jobId` = :jobId", nativeQuery = true)
	List<Object[]> findOptimizedJobsByJobId(@Param("jobId") String jobId);

	@Query(value = " SELECT `jobs`.`id`, `jobs_statuses`.`id` AS `jobs_statuses.id`,"
			+ " `jobs_statuses`.`job_status` AS `jobs_statuses.job_status`,"
			+ " `jobs_statuses`.`job_status_message` AS `jobs_statuses.job_status_message`,"
			+ " `jobs_statuses`.`job_status_data` AS `jobs_statuses.job_status_data` FROM `jobs` AS `jobs`"
			+ "  INNER JOIN `jobs_statuses` AS `jobs_statuses` ON `jobs`.`id` = `jobs_statuses`.`job_id`"
			+ "  WHERE `jobs`.`jobId` = :jobId", nativeQuery = true)
	List<Object[]> findJobStatus(@Param("jobId") String jobId);

}
