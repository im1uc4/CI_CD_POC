package sg.com.ncs.brain.entities.model;

import java.io.Serializable;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sg.com.ncs.brain.entities.Audit;
import sg.com.ncs.brain.model.dataset.Datasets;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "models", indexes = { @Index(name = "model_idx", columnList = "optimisationModelId", unique = true) })
public class Model extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	// private String grantforUser;
	// private String name;
	// private String titleDescription;
	// private String description;

	@Column(name = "optimisationModelId")
	private String optimisationModelId;
	@Column(nullable = false, columnDefinition = "INTEGER DEFAULT 1", name = "versionNum")
	private Integer versionNum;

	@Column(name = "model_name")
	private String modelName;

	@Column(name = "model_description_header")
	private String modelDescriptionHeader;

	@Column(name = "model_description_details")
	private String modelDescriptionDetails;
	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true", name = "model_status")
	private Boolean modelStatus;

	@JsonProperty(access = Access.WRITE_ONLY)
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "model")
	private List<ModelAccess> modelAccessList = new ArrayList<>();

	/*
	 * @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "model_id", referencedColumnName = "id")
	 * 
	 * @JsonManagedReference(value = "model_id") private List<ModelAccess>
	 * modelAccessList = new ArrayList<>();
	 */

	/*
	 * @JsonProperty(access = Access.WRITE_ONLY)
	 * 
	 * @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy =
	 * "model") private List<Parameter> parameterList = new ArrayList<>();
	 */

	/*
	 * @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "model_id", referencedColumnName = "id")
	 * 
	 * @JsonManagedReference(value = "model_id") private List<Constraints>
	 * constraintList = new ArrayList<>();
	 */
	@JsonProperty(access = Access.WRITE_ONLY)
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "model")
	private List<Constraints> constraintList = new ArrayList<>();

	/*
	 * @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "model_id", referencedColumnName = "id")
	 * 
	 * @JsonManagedReference(value = "model_id") private List<Jobs> jobList = new
	 * ArrayList<>();
	 */
	@JsonProperty(access = Access.WRITE_ONLY)
	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "model", fetch = FetchType.LAZY)
	private List<Jobs> jobList = new ArrayList<>();

	/*
	 * @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "model_id", referencedColumnName = "id")
	 * 
	 * @JsonManagedReference(value = "model_id") private List<ModelCustomFields>
	 * modelCustomFieldList = new ArrayList<>();
	 */
	@JsonProperty(access = Access.WRITE_ONLY)
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "model")
	private List<ModelCustomFields> modelCustomFieldList = new ArrayList<>();

	/*
	 * @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "model_id", referencedColumnName = "id")
	 * 
	 * @JsonManagedReference(value = "model_id") private List<Datasets> dataSetList
	 * = new ArrayList<>();
	 */
	@JsonProperty(access = Access.WRITE_ONLY)
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "model")
	private List<Datasets> dataSetList = new ArrayList<>();

	/*
	 * @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "model_id", referencedColumnName = "id")
	 * 
	 * @JsonManagedReference(value = "model_id") private List<Algorithm>
	 * algorithmList = new ArrayList<>();
	 */
	@JsonProperty(access = Access.WRITE_ONLY)
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "model")
	private List<Algorithm> algorithmList = new ArrayList<>();

	/*
	 * @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "model_id", referencedColumnName = "id")
	 * 
	 * @JsonManagedReference(value = "model_id") private
	 * List<ModelDatasetSkeletonTables> modelDatasetSkeletonTableList = new
	 * ArrayList<>();
	 */
	@JsonProperty(access = Access.WRITE_ONLY)
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "model")
	private List<ModelDatasetSkeletonTables> modelDatasetSkeletonTableList = new ArrayList<>();

}