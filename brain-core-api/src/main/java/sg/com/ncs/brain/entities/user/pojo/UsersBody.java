package sg.com.ncs.brain.entities.user.pojo;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsersBody implements Serializable {

	private static final long serialVersionUID = 4193921919529478751L;

	private String username;
	private String name;
	private String fullname;
	private Boolean status;

	private String dutyTitle;
	private String email;
	private String password;

	private List<String> roles;
	private List<String> groups;

}