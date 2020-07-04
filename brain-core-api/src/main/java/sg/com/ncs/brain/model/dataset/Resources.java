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
@Table(name = "resource")
public class Resources extends Audit implements Serializable {

	private static final long serialVersionUID = -7080466548278057101L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String resourceId;
	private String resourceName;

	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
	private Boolean status;

	@ManyToOne
	private Locations locations;

	/*
	 * @OneToMany(mappedBy = "resources") private List<Task> tasks = new
	 * ArrayList<>();
	 * 
	 * @OneToMany(mappedBy = "resources") private List<Skills> skills = new
	 * ArrayList<>();
	 */

	 
	 

	@ManyToOne
	/*
	 * @JoinColumn(name = "datasets_id", referencedColumnName = "id")
	 * 
	 * @JsonBackReference(value = "datasets_id")
	 */
	private Datasets dataset;

	/*
	 * @OneToMany(cascade = { CascadeType.ALL })
	 * 
	 * @JsonManagedReference(value = "resources_id")
	 * 
	 * @JoinColumn(name = "resources_id", referencedColumnName = "id") private
	 * List<ResourcesCustomFields> customFieldList = new ArrayList<>();
	 */

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "resources")
	private List<ResourcesCustomFields> customFieldList = new ArrayList<>();

	/*
	 * @OneToMany(cascade = { CascadeType.ALL })
	 * 
	 * @JsonManagedReference(value = "resources_id")
	 * 
	 * @JoinColumn(name = "resources_id", referencedColumnName = "id") private
	 * List<ResourcesTimeWindow> timeWindowIdList = new ArrayList<>();
	 */

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "resources")
	private List<ResourcesTimeWindow> timeWindowIdList = new ArrayList<>();

}