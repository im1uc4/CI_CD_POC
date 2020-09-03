package sg.com.ncs.brain.entities.model;

import java.io.Serializable;

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
@Table(name = "general_fields_templates")
public class GeneralFieldsTemplates extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, name = "table_id")
	private Integer tableId;

	@Column(nullable = false, name = "column_name")
	private String columnName;

	@Column(nullable = false, name = "fieldSeqNo")
	private Integer fieldSeqNo;

	@Column(name = "display_name")
	private String displayName;

	@Column(nullable = false, name = "column_type")
	private String columnType;
	@Column(nullable = false, name = "control_type")
	private String controlType;
	@Column(name = "column_format")
	private String columnFormat;

	@Column(nullable = false, name = "is_dynamic", columnDefinition = "BOOLEAN DEFAULT false")
	private Boolean isDynamic;
	@Column(nullable = false, name = "is_mandatory", columnDefinition = "BOOLEAN DEFAULT false")
	private Boolean isMandatory;
	@Column(nullable = false, name = "is_visible", columnDefinition = "BOOLEAN DEFAULT true")
	private Boolean isVisible;

	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
	private Boolean status;

//	@ManyToOne
//	private GeneralTablesTemplates table;

}