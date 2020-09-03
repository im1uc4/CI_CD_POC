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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sg.com.ncs.brain.entities.Audit;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "constraint_params")
public class ConstraintParam extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "parameterId")
	private String parameterId;

	@Column(name = "parameterType")
	private String parameterType;

	@Column(name = "parameterNum")
	private String parameterNum;

	private String name;

	@ManyToOne
	/*
	 * @JoinColumn(name = "const_type_id", referencedColumnName = "id")
	 * 
	 * @JsonBackReference(value = "const_type_id")
	 */
	// @JoinColumn(name = "constraint_type_id")

	@JoinColumn(name = "constraint_type_id", referencedColumnName = "id")
	@JsonBackReference(value = "constraint_type_id")
	private ConstraintType constraintType;

	/*
	 * @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "const_param_id", referencedColumnName = "id")
	 * 
	 * @JsonManagedReference(value = "const_param_id") private
	 * List<ConstraintParamStructure> constraintParamStructure = new ArrayList<>();
	 */

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "constraint_param_id", referencedColumnName = "id")
	@JsonManagedReference(value = "constraint_param_id")
	private List<ConstraintParamStructure> constraintParamStructure = new ArrayList<>();

	/*
	 * @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "const_param_id", referencedColumnName = "id")
	 * 
	 * @JsonManagedReference(value = "const_param_id") private
	 * List<ConstraintParamValue> constraintParamValue = new ArrayList<>();
	 */

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "constraint_param_id", referencedColumnName = "id")
	@JsonManagedReference(value = "constraint_param_id")
	private List<ConstraintParamValue> constraintParamValue = new ArrayList<>();

}