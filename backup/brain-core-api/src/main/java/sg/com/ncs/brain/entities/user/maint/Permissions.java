package sg.com.ncs.brain.entities.user.maint;

import java.io.Serializable;

import javax.persistence.CascadeType;
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
//@Audited
@Table(name = "permissions")
public class Permissions extends Audit implements Serializable {

	private static final long serialVersionUID = 4938460347197306720L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private Integer level;
	private String title;

	/*
	 * @Id
	 * 
	 * @ManyToOne
	 * 
	 * @JoinColumn(name = "roles_id", referencedColumnName = "id")
	 * 
	 * @JsonBackReference
	 */
	@ManyToOne(cascade = { CascadeType.ALL })
	private Roles role;

	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
	private Boolean status;

}