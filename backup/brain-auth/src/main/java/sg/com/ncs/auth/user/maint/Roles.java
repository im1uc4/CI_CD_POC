package sg.com.ncs.auth.user.maint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sg.com.ncs.auth.model.Audit;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "roles")
//@Audited
public class Roles extends Audit implements Serializable {

	private static final long serialVersionUID = -8167070300418709493L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;
	private Integer power;

	@JoinColumn(name = "roleId", referencedColumnName = "id")
	@JsonManagedReference(value = "roleId")
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JsonProperty(access = Access.WRITE_ONLY)
	private List<UserRole> userRoles = new ArrayList<>();

	/*
	 * @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "roles_id", referencedColumnName = "id")
	 * 
	 * @JsonManagedReference private List<Permissions> permissions = new
	 * ArrayList<>();
	 */

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "role")
	@JsonProperty(access = Access.WRITE_ONLY)
	private List<Permissions> permisisons = new ArrayList<>();

	private Boolean status;

}