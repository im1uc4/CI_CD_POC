package sg.com.ncs.brain.model.dataset;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import sg.com.ncs.brain.entities.Audit;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "services")
public class Services extends Audit implements Serializable {

	private static final long serialVersionUID = -8491138945814012127L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer serviceId;

	private Integer defaultTaskDuration;

	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
	private Boolean status;

	/*
	 * @OneToMany(mappedBy = "services") private List<Task> tasks = new
	 * ArrayList<>();
	 */

	@ManyToOne
	/*
	 * @JsonBackReference(value = "datasets_id")
	 * 
	 * @JoinColumn(name = "datasets_id", referencedColumnName = "id")
	 */
	private Datasets dataset;

	/*
	 * @OneToMany(cascade = { CascadeType.ALL })
	 * 
	 * @JsonManagedReference(value = "services_id")
	 * 
	 * @JoinColumn(name = "services_id", referencedColumnName = "id") private
	 * List<ServicesCustomFields> customFieldList = new ArrayList<>();
	 */

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "services")
	private List<ServicesCustomFields> customFieldList = new ArrayList<>();

}