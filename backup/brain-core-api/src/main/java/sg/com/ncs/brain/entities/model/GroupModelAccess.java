package sg.com.ncs.brain.entities.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonSetter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "group_model_access")
public class GroupModelAccess implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@JsonSetter("model_name")
	@Column(name = "model_name")
	private String modelName;

	@Column(name = "model_description_header")
	@JsonSetter("model_description_header")
	private String modelDescriptionHeader;

	@JsonSetter("model_description_details")
	@Column(name = "model_description_details")
	private String modelDescriptionDetails;
	@JsonSetter("person_in_charge")
	@Column(name = "person_in_charge")
	private String personInCharge;

	@JsonSetter("created_by")
	@Column(name = "created_by")
	private String createdBy;

	@JsonSetter("user_name")
	@Column(name = "username")
	@JsonProperty(access = Access.WRITE_ONLY)
	private String username;

	@JsonSetter("access_type")
	@Column(name = "access_type")
	@JsonProperty(access = Access.WRITE_ONLY)
	private String accessType;
}