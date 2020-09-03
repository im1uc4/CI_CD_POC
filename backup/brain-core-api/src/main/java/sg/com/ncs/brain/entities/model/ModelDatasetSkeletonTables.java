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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import javax.persistence.JoinColumn;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import sg.com.ncs.brain.entities.Audit;
import sg.com.ncs.brain.model.dataset.DatasetData;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "model_dataset_skeleton_tables", indexes = {
		@Index(name = "model_dataset_index", columnList = "table_name,model_id,status", unique = true) })
public class ModelDatasetSkeletonTables extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, name = "table_name")
	private String tableName;
	@Column(name = "display_name")
	private String displayName;

	@Column(nullable = false, name = "tableSeqNo")
	private Integer tableSeqNo;

	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true", name = "is_editable")
	private Boolean isEditable;

	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
	private Boolean status;

	@ManyToOne
	@JsonProperty(access = Access.WRITE_ONLY)
	private Model model;

	@JsonProperty(access = Access.WRITE_ONLY)
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "table")
	private List<ModelDatasetSkeletonFields> modelDatasetSkeletonFields = new ArrayList<>();

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "dataset_skeleton_table_id", referencedColumnName = "id")
	@JsonManagedReference(value = "dataset_skeleton_table_id")
	private List<DatasetData> datasetData = new ArrayList<>();

}