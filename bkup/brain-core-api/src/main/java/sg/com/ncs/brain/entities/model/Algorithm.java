package sg.com.ncs.brain.entities.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sg.com.ncs.brain.entities.Audit;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "algorithm")
public class Algorithm extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	// private Integer model_id;
	private String name;
	private Boolean status;

	@ManyToOne
	/*
	 * @JoinColumn(name = "model_id", referencedColumnName = "id")
	 * 
	 * @JsonBackReference(value = "model_id")
	 */
	@JsonProperty(access = Access.WRITE_ONLY)
	private Model model;

	@ManyToOne
	/*
	 * @JoinColumn(name = "algo_type_id", referencedColumnName = "id")
	 * 
	 * @JsonBackReference(value = "algo_type_id")
	 */
	private AlgorithmType algorithm_type;

}