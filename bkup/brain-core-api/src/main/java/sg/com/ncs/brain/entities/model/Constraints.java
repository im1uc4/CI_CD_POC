package sg.com.ncs.brain.entities.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.JoinColumn;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sg.com.ncs.brain.entities.Audit;
import com.fasterxml.jackson.annotation.JsonSetter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "constraints")
public class Constraints extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "constraintId")
	private String constraintId;

	@Column(name = "constraintNum")
	private Integer constraintNum;

	@JsonSetter("constraintName")
	@Column(name = "displayName")
	private String displayName;

	@Column(/* nullable = false, */columnDefinition = "BOOLEAN DEFAULT true", name = "status")
	private Boolean status;

	@JsonSetter("scoreLeveldescription")
	@Column(name = "scoreLevel")
	private String scoreLevel;

	@Column(name = "scoreWeight")
	private String scoreWeight;

	/*
	 * @ManyToOne
	 * 
	 * @JoinColumn(name = "model_id", referencedColumnName = "id")
	 * 
	 * @JsonBackReference(value = "model_id") private Model model;
	 */

	@ManyToOne
	// @JoinColumn(name = "model_id")
	// @JsonBackReference(value = "model_id")
	// @JsonProperty(access = Access.WRITE_ONLY)
	private Model model;

	/*
	 * @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "constraing_id", referencedColumnName = "id")
	 * 
	 * @JsonManagedReference(value = "constraint_id") private List<ConstraintMatch>
	 * matchList = new ArrayList<>();
	 */
	@JsonProperty(access = Access.WRITE_ONLY)
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "constraint")
	private List<ConstraintMatch> matchList = new ArrayList<>();

	/*
	 * @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "constraint_id", referencedColumnName = "id")
	 * 
	 * @JsonManagedReference(value = "constraint_id") private
	 * List<ConstraintParamValue> paramList = new ArrayList<>();
	 */
	@JsonProperty(access = Access.WRITE_ONLY)
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "constraint")
	private List<ConstraintParamValue> paramList = new ArrayList<>();

	@ManyToOne
	/*
	 * @JoinColumn(name = "const_type_id", referencedColumnName = "id")
	 * 
	 * @JsonBackReference(value = "const_type_id")
	 */
	@JsonBackReference(value = "constraint_type_id")
	@JoinColumn(name = "constraint_type_id", referencedColumnName = "id")
	// @JsonProperty(access = Access.WRITE_ONLY)
	private ConstraintType constraintType;

}