package sg.com.ncs.brain.entities.user.maint;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sg.com.ncs.brain.entities.Audit;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_roles")
public class UserRole extends Audit implements Serializable {

	private static final long serialVersionUID = 513099894385064463L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(cascade = { CascadeType.MERGE })
	@JoinColumn(name = "roleId", referencedColumnName = "id")
	@JsonBackReference(value = "roleId")
	private Roles roles;

	@ManyToOne(cascade = { CascadeType.MERGE })
	@JoinColumn(name = "usersId", referencedColumnName = "id")
	@JsonBackReference(value = "usersId")
	private User user;

}