package sg.com.ncs.brain.entities.model;

import java.io.Serializable;

 
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
@Table(name = "constraint_param_value_lists")
public class ConstraintParamValueLists implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer constraint_id;
	private Integer constraint_param_id;

	private String value;

	 
	 

	 
	private Audit Audit;

}