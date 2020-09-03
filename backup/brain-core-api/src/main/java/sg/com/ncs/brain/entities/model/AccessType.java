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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sg.com.ncs.brain.entities.Audit;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "access_types")
public class AccessType extends Audit implements Serializable {

	private static final long serialVersionUID = -2690706133407358760L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "access_type")
	private String accessType;

	/*
	 * @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "access_type_id", referencedColumnName = "id")
	 * 
	 * @JsonManagedReference(value = "access_type_id") private List<ModelAccess>
	 * modelAccess = new ArrayList<>();
	 */

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "access_type_id", referencedColumnName = "id")
	@JsonManagedReference(value = "access_type_id")

	private List<ModelAccess> modelAccess = new ArrayList<>();

	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
	private Boolean status;

}