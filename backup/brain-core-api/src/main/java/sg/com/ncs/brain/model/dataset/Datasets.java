package sg.com.ncs.brain.model.dataset;

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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sg.com.ncs.brain.entities.Audit;
import sg.com.ncs.brain.entities.model.Jobs;
import sg.com.ncs.brain.entities.model.Model;

@Entity
@Getter
@Setter
@NoArgsConstructor
//@Audited
@Table(name = "datasets", indexes = {
		@Index(name = "dataset_idx1", columnList = "model_id,status,is_masterds", unique = false),
		@Index(name = "dataset_idx2", columnList = "datasetId,name,is_masterds,status", unique = false) })
public class Datasets extends Audit implements Serializable {

	private static final long serialVersionUID = 706927729278397664L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "datasetId")
	private String datasetId;

	private String name;
	private String description;

	@Column(name = "scheduleDateTime")
	// @Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime scheduleDateTime;

	@Column(name = "is_masterds", nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
	private Boolean isMasterds;

	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
	private Boolean status;

	@ManyToOne
	/*
	 * @JoinColumn(name = "model_id", referencedColumnName = "id")
	 * 
	 * @JsonBackReference(value = "model_id")
	 */
	@JsonProperty(access = Access.WRITE_ONLY)
	private Model model;

	/*
	 * @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "datasets_id", referencedColumnName = "id")
	 * 
	 * @JsonManagedReference(value = "datasets_id") private List<Resources>
	 * resourceList = new ArrayList<>();
	 */
	@JsonProperty(access = Access.WRITE_ONLY)
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "dataset")
	private List<Resources> resourceList = new ArrayList<>();

	/*
	 * @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "datasets_id", referencedColumnName = "id")
	 * 
	 * @JsonManagedReference(value = "datasets_id") private List<Task> taskList =
	 * new ArrayList<>();
	 */
	@JsonProperty(access = Access.WRITE_ONLY)
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "dataset")
	private List<Task> taskList = new ArrayList<>();

	/*
	 * @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "datasets_id", referencedColumnName = "id")
	 * 
	 * @JsonManagedReference(value = "datasets_id") private List<Services>
	 * serviceList = new ArrayList<>();
	 */
	@JsonProperty(access = Access.WRITE_ONLY)
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "dataset")
	private List<Services> serviceList = new ArrayList<>();

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "dataset")
	private List<DatasetData> datasetData = new ArrayList<>();

	/*
	 * @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "datasets_id", referencedColumnName = "id")
	 * 
	 * @JsonManagedReference(value = "datasets_id") private List<TimeWindow>
	 * timeWindowList = new ArrayList<>();
	 */
	@JsonProperty(access = Access.WRITE_ONLY)
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "dataset")
	private List<TimeWindow> timeWindowList = new ArrayList<>();

	@JsonProperty(access = Access.WRITE_ONLY)
	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "dataset", fetch = FetchType.LAZY)
	private List<Jobs> jobList = new ArrayList<>();

}