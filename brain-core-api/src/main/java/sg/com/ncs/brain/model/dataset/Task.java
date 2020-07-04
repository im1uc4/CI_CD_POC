package sg.com.ncs.brain.model.dataset;

import java.io.Serializable;
import java.sql.Date;
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
@Table(name = "task")
public class Task extends Audit implements Serializable {

	private static final long serialVersionUID = 8184746779882001423L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer taskId;
	private Date startDatetime;

	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
	private Boolean status;

	@ManyToOne
	private Locations locations;

	/*
	 * @ManyToOne private Resources resources;
	 */

	private Integer taskDuration;
	private Boolean resourceLocked;
	private Date startDateTimeLocked;

	/*
	 * @ManyToOne private Services services;
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
	 * @JsonManagedReference(value = "task_id")
	 * 
	 * @JoinColumn(name = "task_id", referencedColumnName = "id") private
	 * List<TaskTimeWindows> timeWindowList = new ArrayList<>();
	 */

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "task")
	private List<TaskTimeWindows> timeWindowList = new ArrayList<>();

}