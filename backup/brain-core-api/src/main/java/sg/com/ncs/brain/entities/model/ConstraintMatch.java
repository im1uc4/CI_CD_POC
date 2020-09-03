package sg.com.ncs.brain.entities.model;

import java.io.Serializable;

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
@Table(name = "constraint_match")
public class ConstraintMatch extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer taskId;
	private Long score;

	@ManyToOne
	/*
	 * @JoinColumn(name = "constraint_id", referencedColumnName = "id")
	 * 
	 * @JsonBackReference(value = "constraint_id")
	 */
	private Constraints constraint;

}