package sg.com.ncs.brain.model.dataset;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sg.com.ncs.brain.entities.Audit;
import sg.com.ncs.brain.entities.model.ModelDatasetSkeletonFields;
import sg.com.ncs.brain.entities.model.ModelDatasetSkeletonTables;

@Entity
@Getter
@Setter
@NoArgsConstructor
//@Audited
@Table(name = "dataset_data")
public class DatasetData extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "record_id")
	private Integer recordId;
	private String value;

	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
	private Boolean status;

	@ManyToOne
	/*
	 * @JoinColumn(name = "dataset_id", referencedColumnName = "id")
	 * 
	 * @JsonBackReference(value = "dataset_id")
	 */
	private Datasets dataset;

	@ManyToOne
	/*
	 * @JoinColumn(name = "model_data_skeltn_table_id", referencedColumnName = "id")
	 * 
	 * @JsonBackReference(value = "model_data_skeltn_table_id")
	 */

	@JsonBackReference(value = "dataset_skeleton_table_id")
	@JoinColumn(name = "dataset_skeleton_table_id",referencedColumnName = "id")
	private ModelDatasetSkeletonTables datasetSkeletonTable;

	@ManyToOne
	/*
	 * @JoinColumn(name = "model_data_skeltn_field_id", referencedColumnName = "id")
	 * 
	 * @JsonBackReference(value = "model_data_skeltn_field_id")
	 */
	@JsonBackReference(value = "dataset_skeleton_field_id")
	@JoinColumn(name = "dataset_skeleton_field_id",referencedColumnName = "id")
	private ModelDatasetSkeletonFields datasetSkeletonField;

}