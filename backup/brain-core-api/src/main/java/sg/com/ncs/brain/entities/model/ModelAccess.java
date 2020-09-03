package sg.com.ncs.brain.entities.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sg.com.ncs.brain.entities.Audit;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "model_accesses")
public class ModelAccess extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "target_id")
	private Integer targetId;
	private Boolean status;

	@ManyToOne
	/*
	 * @JoinColumn(name = "access_type_id", referencedColumnName = "id")
	 * 
	 * @JsonBackReference(value = "access_type_id")
	 */

	@JoinColumn(name = "access_type_id", referencedColumnName = "id")
	@JsonBackReference(value = "access_type_id")
	private AccessType accessType;

	@ManyToOne
	/*
	 * @JoinColumn(name = "model_id", referencedColumnName = "id")
	 * 
	 * @JsonBackReference(value = "model_id")
	 */
	private Model model;

}