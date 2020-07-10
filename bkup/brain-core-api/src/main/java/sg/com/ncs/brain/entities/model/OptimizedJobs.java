package sg.com.ncs.brain.entities.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sg.com.ncs.brain.entities.Audit;

@Entity
@Getter
@Setter
@NoArgsConstructor
//@Audited
@Table(name = "optimized_jobs")
public class OptimizedJobs extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "taskId")
	private String taskId;
	@Column(name = "taskName")
	private String taskName;
	@Column(name = "startDateTime")
	private String startDateTime;
	@Column(name = "endDateTime")
	private String endDateTime;
	@Column(name = "taskDuration")
	private Integer taskDuration;
	@Column(name = "resourceLocked")
	private Boolean resourceLocked;
	@Column(name = "startDateTimeLocked")
	private Boolean startDateTimeLocked;

	@Column(name = "customFieldList")
	private String customFieldList;
	@Column(name = "serviceId")
	private String serviceId;
	@Column(name = "timeWindowId")
	private String timeWindowId;
	@Column(name = "resourceId")
	private String resourceId;
	private String location;

	@ManyToOne(cascade = CascadeType.ALL)
	// @JsonProperty(access = Access.WRITE_ONLY)
	/*
	 * @JoinColumn(name = "job_id", referencedColumnName = "id")
	 * 
	 * @JsonBackReference(value = "job_id")
	 */
	private Jobs job;

}