package sg.com.ncs.brain.entities.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sg.com.ncs.brain.entities.Audit;
import sg.com.ncs.brain.model.dataset.Datasets;

@Entity
@Getter
@Setter
@NoArgsConstructor
//@Audited
@Table(name = "jobs", indexes = { @Index(name = "jobs_idx", columnList = "jobId", unique = true) })

public class Jobs extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "jobId")
	private String jobId;
	// private Integer model_id;
	// private Integer datasetId;
	private String description;

	@Column(name = "completed_datetime")
	//@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime completedDateTime;

	@Column(name = "submitted_datetime")
	//@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime submittedDatetime;
	private Boolean status;

	// @JsonProperty(access = Access.WRITE_ONLY)
	@ManyToOne
	/*
	 * @JoinColumn(name = "model_id", referencedColumnName = "id")
	 * 
	 * @JsonBackReference(value = "model_id")
	 */
	private Model model;

	@ManyToOne
	/*
	 * @JoinColumn(name = "model_id", referencedColumnName = "id")
	 * 
	 * @JsonBackReference(value = "model_id")
	 */
	private Datasets dataset;
	// private Datasets datasets;

	/*
	 * @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "job_id", referencedColumnName = "id")
	 * 
	 * @JsonManagedReference(value = "job_id") private List<JobsSettings>
	 * jobSettings = new ArrayList<>();
	 */
	// @JsonProperty(access = Access.WRITE_ONLY)
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "job")
	private List<JobsSettings> jobSettings = new ArrayList<>();

	/*
	 * @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "job_id", referencedColumnName = "id")
	 * 
	 * @JsonManagedReference(value = "job_id") private List<JobsStatusUpdate>
	 * jobsStatusUpdate = new ArrayList<>();
	 */
	// @JsonProperty(access = Access.WRITE_ONLY)
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "job")
	private List<JobsStatusUpdate> jobsStatusUpdate = new ArrayList<>();

	/*
	 * @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "job_id", referencedColumnName = "id")
	 * 
	 * @JsonManagedReference(value = "job_id") private List<OptimizedJobs>
	 * OptimizedJobs = new ArrayList<>();
	 */
	// @JsonProperty(access = Access.WRITE_ONLY)
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "job")
	private List<OptimizedJobs> OptimizedJobs = new ArrayList<>();

}