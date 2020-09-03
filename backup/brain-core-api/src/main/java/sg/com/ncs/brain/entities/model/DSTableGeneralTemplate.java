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
@Table(name = "dataset_general_template")
public class DSTableGeneralTemplate implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private Integer model_id;

	@Column(nullable = false)
	private String table_name;

	private String display_name;

	@Column(nullable = false)
	private Integer tableSeqNo;

	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
	private Boolean is_editable;

	 
	 

	 
	private Audit Audit;

	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
	private Boolean status;

}