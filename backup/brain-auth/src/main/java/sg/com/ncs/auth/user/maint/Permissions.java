package sg.com.ncs.auth.user.maint;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sg.com.ncs.auth.model.Audit;

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

	@ManyToOne
	private Roles role;

	private Boolean status;

}