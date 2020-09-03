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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sg.com.ncs.auth.model.Audit;

@Entity
@Getter
@Setter
@NoArgsConstructor
//@Audited
@Table(name = "\"groups\"")
public class Groups extends Audit implements Serializable {

	private static final long serialVersionUID = 2436691510240200061L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	private Boolean status;

	/*
	 * @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "groups_id", referencedColumnName = "id")
	 * 
	 * @JsonManagedReference(value = "groups_id") private List<UserGroups>
	 * userGroups = new ArrayList<>();
	 */

	// @JsonProperty(access = Access.WRITE_ONLY)

	@JoinColumn(name = "groupsId", referencedColumnName = "id")
	@JsonManagedReference(value = "groupsId")
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	private List<UserGroups> userGroups = new ArrayList<>();

}