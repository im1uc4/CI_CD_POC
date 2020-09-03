package sg.com.ncs.brain.model.dataset;

import java.io.Serializable;

import javax.persistence.Column;
 
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
@Table(name = "task_timewindows")
public class TaskTimeWindows extends Audit implements Serializable {

	private static final long serialVersionUID = 8000087482258295681L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
	private Boolean status;

	 
	 

	@ManyToOne
	/*
	 * @JsonBackReference(value = "task_id")
	 * 
	 * @JoinColumn(name = "task_id", referencedColumnName = "id")
	 */
	private Task task;

	@ManyToOne
	/*
	 * @JsonBackReference(value = "timeWindow_id")
	 * 
	 * @JoinColumn(name = "timewindow_id", referencedColumnName = "id")
	 */
	private TimeWindow timeWindow;
}