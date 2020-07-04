package sg.com.ncs.brain.entities.user.maint;

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
import javax.persistence.Index;
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
//@Audited
@Table(name = "users", indexes = { @Index(name = "users_idx", columnList = "username", unique = true) })
public class User extends Audit implements Serializable {

	private static final long serialVersionUID = 4193921919526248751L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String username;
	private String name;
	private String fullname;

	@Column(name = "duty_title")
	private String dutyTitle;
	private String email;
	private String password;

	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
	private Boolean status;

	/*
	 * @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "users_id", referencedColumnName = "id")
	 * 
	 * @JsonManagedReference(value = "users_id") private List<UserGroups> userGroups
	 * = new ArrayList<>();
	 */
	@JoinColumn(name = "usersId", referencedColumnName = "id")
	@JsonManagedReference(value = "usersId")
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	// @JsonProperty(access = Access.WRITE_ONLY)
	private List<UserGroups> userGroups = new ArrayList<>();

	/*
	 * @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "users_id", referencedColumnName = "id")
	 * 
	 * @JsonManagedReference(value = "users_id") private List<UserRole> userRoles =
	 * new ArrayList<>();
	 */

	@JoinColumn(name = "usersId", referencedColumnName = "id")
	@JsonManagedReference(value = "usersId")
	// @JsonProperty(access = Access.WRITE_ONLY)
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	private List<UserRole> userRoles = new ArrayList<>();

}