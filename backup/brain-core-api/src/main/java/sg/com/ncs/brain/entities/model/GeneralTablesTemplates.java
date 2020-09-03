package sg.com.ncs.brain.entities.model;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sg.com.ncs.brain.entities.Audit;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "general_tables_templates")
public class GeneralTablesTemplates extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="table_name")
	private String tableName;
	@Column(name="display_name")
	private String displayName;

	@Column(nullable = false, name = "tableSeqNo")
	private Integer tableSeqNo;

	@Column(nullable = false, name = "is_editable", columnDefinition = "BOOLEAN DEFAULT true")
	private Boolean isEditable;

	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
	private Boolean status;

	// @JsonProperty(access = Access.WRITE_ONLY)
//	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "table", fetch = FetchType.LAZY)
//	private List<GeneralFieldsTemplates> generalFieldsTemplates = new ArrayList<>();

}