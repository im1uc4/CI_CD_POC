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
import javax.persistence.JoinColumn;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "model_dataset_skeleton_fields", indexes = {
		@Index(name = "model_dataset_index", columnList = "table_id,column_name,status", unique = true) })
public class ModelDatasetSkeletonFields extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

//	@Column(nullable = false, name = "table_id")
//	private Integer tableId;
	@Column(nullable = false, name = "column_name")
	private String columnName;
	@Column(nullable = false, name = "fieldSeqNo")
	private Integer fieldSeqNo;
	@Column(name = "display_name")
	private String displayName;
	@Column(nullable = false, name = "column_type")
	private String columnType;
	@Column(nullable = false, name = "control_type")
	private String controlType;
	@Column(name = "column_format")
	private String columnFormat;

	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false", name = "is_dynamic")
	private Boolean isDynamic;
	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false", name = "is_mandatory")
	private Boolean isMandatory;
	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true", name = "is_visible")
	private Boolean isVisible;

	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
	private Boolean status;

	@ManyToOne
	private ModelDatasetSkeletonTables table;

	@JoinColumn(name = "dataset_skeleton_field_id", referencedColumnName = "id")
	@JsonManagedReference(value = "dataset_skeleton_field_id")
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	private List<DatasetData> datasetData = new ArrayList<>();

}