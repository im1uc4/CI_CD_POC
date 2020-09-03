package sg.com.ncs.brain.model.dataset;

import java.io.Serializable;

import javax.persistence.Column;
 
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
 
import sg.com.ncs.brain.entities.Audit;

@Entity
@Getter
@Setter
@NoArgsConstructor
//@Audited
@Table(name = "services_custom_fields")
public class ServicesCustomFields extends Audit implements Serializable {

	private static final long serialVersionUID = 2309985089172268639L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String value;
	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
	private Boolean status;

	 
	 

	@ManyToOne
	/*
	 * @JoinColumn(name = "services_id", referencedColumnName = "id")
	 * 
	 * @JsonBackReference(value = "services_id")
	 */
	private Services services;
}