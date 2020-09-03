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
@Table(name = "constrtype_param")
public class ConstraintTypeParam implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer constrTypeId;
	private String parameterNum;
	private String name;
	private String description;

	private Boolean status;
	private Boolean mandatory;
	private Boolean editable;
	private String datatype;
	private String value;

	 
	 

	 
	private Audit Audit;

}