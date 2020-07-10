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
@Table(name = "timewindow")
public class TimeWindow extends Audit implements Serializable {

	private static final long serialVersionUID = 2278352796366579964L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String timeWindowId;

	private Date startDateTime;

	private Date endDateTime;

	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
	private Boolean status;

	 
	 

	@ManyToOne
	/*
	 * @JsonBackReference(value = "datasets_id")
	 * 
	 * @JoinColumn(name = "datasets_id", referencedColumnName = "id")
	 */
	private Datasets dataset;

	/*
	 * @OneToMany
	 * 
	 * @JsonManagedReference(value = "timeWindow_id")
	 * 
	 * @JoinColumn(name = "timewindow_id", referencedColumnName = "id") private
	 * List<TaskTimeWindows> taskTimeWindowList = new ArrayList<>();
	 */

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "timeWindow")
	private List<TaskTimeWindows> taskTimeWindowList = new ArrayList<>();

}