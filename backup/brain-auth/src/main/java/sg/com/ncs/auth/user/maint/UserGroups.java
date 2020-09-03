package sg.com.ncs.auth.user.maint;

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
import sg.com.ncs.auth.model.Audit;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_groups")
public class UserGroups extends Audit implements Serializable {

	private static final long serialVersionUID = -3427685471238341168L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(cascade = { CascadeType.ALL })

	@JoinColumn(name = "groupsId", referencedColumnName = "id")
	@JsonBackReference(value = "groupsId")
	private Groups group;

	@ManyToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "usersId", referencedColumnName = "id")
	@JsonBackReference(value = "usersId")
	private User user;

}