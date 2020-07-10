package sg.com.ncs.brain.model.dataset;

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
@Table(name = "resource_type")
public class ResourcesType implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer resource_type;

	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
	private Boolean resource_status;

	 
	 

	 
	private Audit Audit;

}