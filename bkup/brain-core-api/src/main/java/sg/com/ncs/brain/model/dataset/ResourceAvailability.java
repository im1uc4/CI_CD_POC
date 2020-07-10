package sg.com.ncs.brain.model.dataset;

import java.io.Serializable;
import java.sql.Date;

 
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
@Table(name = "resource_avail")
public class ResourceAvailability implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer resourceId;
	private Date datetime_start;
	private Date datetime_end;
	private Integer record_id;

	 
	 

	 
	private Audit Audit;

}