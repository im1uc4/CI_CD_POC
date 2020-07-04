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
@Table(name = "algorithm_type_param")
public class AlgorithmTypeParam implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer alg_type_id;
	private String parameterNum;
	private String name;
	private String description;
	private Boolean mandatory;

	private Boolean editable;
	private String datatype;
	private String value;

	 
	 

	 
	private Audit Audit;

//	/@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
	private Boolean status;

}