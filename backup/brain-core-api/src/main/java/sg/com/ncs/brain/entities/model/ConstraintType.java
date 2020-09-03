package sg.com.ncs.brain.entities.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "constraint_types")
public class ConstraintType extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "constraintTypeId")
	private String constraintTypeId;

	@Column(name = "constraintTypeName")
	private String constraintTypeName;

	@Column(name = "defaultDisplayName")
	private String defaultDisplayName;

	/*
	 * @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "const_type_id", referencedColumnName = "id")
	 * 
	 * @JsonManagedReference(value = "const_type_id") private List<ConstraintParam>
	 * constraintParam = new ArrayList<>();
	 */

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "constraint_type_id", referencedColumnName = "id")
	@JsonManagedReference(value = "constraint_type_id")
	private List<ConstraintParam> constraintParam = new ArrayList<>();

	/*
	 * @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "const_type_id", referencedColumnName = "id")
	 * 
	 * @JsonManagedReference(value = "const_type_id") private List<Constraints>
	 * constraints = new ArrayList<>();
	 */

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "constraint_type_id", referencedColumnName = "id")
	@JsonManagedReference(value = "constraint_type_id")
	@JsonProperty(access = Access.WRITE_ONLY)
	private List<Constraints> constraints = new ArrayList<>();

}