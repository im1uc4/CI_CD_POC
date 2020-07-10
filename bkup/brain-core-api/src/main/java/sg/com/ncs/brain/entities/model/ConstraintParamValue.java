package sg.com.ncs.brain.entities.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sg.com.ncs.brain.entities.Audit;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "constraint_param_values")
public class ConstraintParamValue extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String value;

	@ManyToOne
	/*
	 * @JoinColumn(name = "const_param_struct_id", referencedColumnName = "id")
	 * 
	 * @JsonBackReference(value = "const_param_struct_id")
	 */
	@JoinColumn(name = "constraint_param_key_id",referencedColumnName = "id")
	@JsonBackReference(value = "constraint_param_key_id")
	private ConstraintParamStructure constraintParamKey;

	@ManyToOne
	/*
	 * @JoinColumn(name = "constraint_id", referencedColumnName = "id")
	 * 
	 * @JsonBackReference(value = "constraint_id")
	 */
	@JsonProperty(access = Access.WRITE_ONLY)
	private Constraints constraint;

	@ManyToOne
	/*
	 * @JoinColumn(name = "const_param_id", referencedColumnName = "id")
	 * 
	 * @JsonBackReference(value = "const_param_id")
	 */
	@JoinColumn(name = "constraint_param_id",referencedColumnName = "id")
	@JsonBackReference(value = "constraint_param_id")
	private ConstraintParam constraintParam;
}