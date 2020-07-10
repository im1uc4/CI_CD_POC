package sg.com.ncs.brain.entities.model;

import java.io.Serializable;

import javax.persistence.Column;
 
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
 
import sg.com.ncs.brain.entities.Audit;

@Entity
@Data
@NoArgsConstructor
@Table(name = "constraint_parm")
public class ConstraintParameters implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer constraint_id;
	private String custom_constraint_name;
	private Boolean status;
	private Boolean param_status;

	private String scoreLevel;
	private String scoreWeight;

	@Column(precision = 10, scale = 2)
	private double constraint_parameter_value;

	 
	 

	 
	private Audit Audit;

}