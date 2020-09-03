package sg.com.ncs.brain.entities.model;

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
@Table(name = "jobs_settings")
public class JobsSettings extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "maxjobduration")
	private Long maxjobduration;

	@Column(name = "optimisationmode_id")
	private String optimisationmodeId;

	@Column(name = "maxsolutionunimprtime")
	private Long maxsolutionunimprtime;

	@ManyToOne
	/*
	 * @JoinColumn(name = "job_id", referencedColumnName = "id")
	 * 
	 * @JsonBackReference(value = "job_id")
	 */
	private Jobs job;

}