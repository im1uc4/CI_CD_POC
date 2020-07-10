package sg.com.ncs.brain.seeders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import sg.com.ncs.brain.controller.UserController;
import sg.com.ncs.brain.entities.model.AccessType;
import sg.com.ncs.brain.entities.model.GeneralFieldsTemplates;
import sg.com.ncs.brain.entities.model.GeneralTablesTemplates;
import sg.com.ncs.brain.entities.user.maint.Groups;
import sg.com.ncs.brain.entities.user.maint.Roles;
import sg.com.ncs.brain.repository.AccessTypeRepository;
import sg.com.ncs.brain.repository.GeneralFieldsTemplatesRepository;
import sg.com.ncs.brain.repository.GeneralTableTemplatesRepository;
import sg.com.ncs.brain.repository.GroupsRepository;
import sg.com.ncs.brain.repository.RolesRepository;
import sg.com.ncs.brain.repository.UserRepository;

@Component
public class DatabaseSeeder {

	Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);

	@Autowired
	UserController userController;

	@Autowired
	UserRepository userRepository;

	@Autowired
	GroupsRepository groupsRepository;

	@Autowired
	GeneralFieldsTemplatesRepository generalFieldsTemplatesRepository;

	@Autowired
	GeneralTableTemplatesRepository generalTableTemplatesRepository;

	@Autowired
	AccessTypeRepository accessTypeRepository;

	@Autowired
	RolesRepository rolesRepository;

	@EventListener
	public void seed(ContextRefreshedEvent event) {
		seedRolesTable();
		seedGroupsTable();
		seedAccessTypeTable();
		seedFieldsTemplateTable();
		seedTablesTemplateTable();
		seedAdminUser();
	}

	private void seedRolesTable() {

		Boolean roleExists = rolesRepository.existsByName("System Administrator");
		Roles initialRoles = new Roles();

		if (!roleExists) {
			initialRoles.setName("System Administrator");
			initialRoles.setPower(3);
			initialRoles.setStatus(true);
			rolesRepository.save(initialRoles);
		}

		initialRoles = new Roles();
		roleExists = rolesRepository.existsByName("Module Administrator");

		if (!roleExists) {
			initialRoles.setName("Module Administrator");
			initialRoles.setPower(2);
			initialRoles.setStatus(true);
			rolesRepository.save(initialRoles);
		}

		initialRoles = new Roles();
		roleExists = rolesRepository.existsByName("Module User");

		if (!roleExists) {
			initialRoles.setName("Module User");
			initialRoles.setPower(1);
			initialRoles.setStatus(true);
			rolesRepository.save(initialRoles);
		}
	}

	private void seedGroupsTable() {

		Boolean groupExists = groupsRepository.existsByName("General");
		if (!groupExists) {
			Groups initialGroups = new Groups();
			initialGroups.setName("General");
			initialGroups.setStatus(true);
			groupsRepository.save(initialGroups);
		}
	}

	private void seedAccessTypeTable() {

		Boolean accessTypeExists = accessTypeRepository.existsByAccessType("user");
		AccessType initialAccessType = new AccessType();

		if (!accessTypeExists) {
			initialAccessType.setAccessType("user");
			initialAccessType.setStatus(true);
			accessTypeRepository.save(initialAccessType);
		}

		initialAccessType = new AccessType();
		accessTypeExists = accessTypeRepository.existsByAccessType("group");

		if (!accessTypeExists) {
			initialAccessType.setAccessType("group");
			initialAccessType.setStatus(true);
			accessTypeRepository.save(initialAccessType);
		}
	}

	private void seedFieldsTemplateTable() {

		Boolean fieldsExists = generalFieldsTemplatesRepository.existsByTableIdAndColumnName(6, "startDateTimeLocked");
		GeneralFieldsTemplates initialGeneralFieldsTemplates = new GeneralFieldsTemplates();

		if (!fieldsExists) {

			initialGeneralFieldsTemplates.setTableId(6);
			initialGeneralFieldsTemplates.setColumnName("startDateTimeLocked");
			initialGeneralFieldsTemplates.setFieldSeqNo(111);
			initialGeneralFieldsTemplates.setDisplayName("Start Date Time Locked");
			initialGeneralFieldsTemplates.setColumnType("numeric");
			initialGeneralFieldsTemplates.setControlType("checkbox");
			initialGeneralFieldsTemplates.setColumnFormat("");
			initialGeneralFieldsTemplates.setIsDynamic(false);
			initialGeneralFieldsTemplates.setIsMandatory(true);
			initialGeneralFieldsTemplates.setIsVisible(true);
			initialGeneralFieldsTemplates.setStatus(true);

			generalFieldsTemplatesRepository.save(initialGeneralFieldsTemplates);
		}

		initialGeneralFieldsTemplates = new GeneralFieldsTemplates();
		fieldsExists = generalFieldsTemplatesRepository.existsByTableIdAndColumnName(6, "resourceLocked");

		if (!fieldsExists) {

			initialGeneralFieldsTemplates.setTableId(6);
			initialGeneralFieldsTemplates.setColumnName("resourceLocked");
			initialGeneralFieldsTemplates.setFieldSeqNo(10);
			initialGeneralFieldsTemplates.setDisplayName("Resource Locked");
			initialGeneralFieldsTemplates.setColumnType("numeric");
			initialGeneralFieldsTemplates.setControlType("checkbox");
			initialGeneralFieldsTemplates.setColumnFormat("");
			initialGeneralFieldsTemplates.setIsDynamic(false);
			initialGeneralFieldsTemplates.setIsMandatory(true);
			initialGeneralFieldsTemplates.setIsVisible(true);
			initialGeneralFieldsTemplates.setStatus(true);

			generalFieldsTemplatesRepository.save(initialGeneralFieldsTemplates);
		}

		initialGeneralFieldsTemplates = new GeneralFieldsTemplates();
		fieldsExists = generalFieldsTemplatesRepository.existsByTableIdAndColumnName(6, "taskDuration");

		if (!fieldsExists) {

			initialGeneralFieldsTemplates.setTableId(6);
			initialGeneralFieldsTemplates.setColumnName("taskDuration");
			initialGeneralFieldsTemplates.setFieldSeqNo(9);
			initialGeneralFieldsTemplates.setDisplayName("Task Duration");
			initialGeneralFieldsTemplates.setColumnType("numeric");
			initialGeneralFieldsTemplates.setControlType("textbox");
			initialGeneralFieldsTemplates.setColumnFormat("");
			initialGeneralFieldsTemplates.setIsDynamic(false);
			initialGeneralFieldsTemplates.setIsMandatory(true);
			initialGeneralFieldsTemplates.setIsVisible(true);
			initialGeneralFieldsTemplates.setStatus(true);

			generalFieldsTemplatesRepository.save(initialGeneralFieldsTemplates);
		}

		initialGeneralFieldsTemplates = new GeneralFieldsTemplates();
		fieldsExists = generalFieldsTemplatesRepository.existsByTableIdAndColumnName(6, "serviceId");

		if (!fieldsExists) {

			initialGeneralFieldsTemplates.setTableId(6);
			initialGeneralFieldsTemplates.setColumnName("serviceId");
			initialGeneralFieldsTemplates.setFieldSeqNo(8);
			initialGeneralFieldsTemplates.setDisplayName("Service Required");
			initialGeneralFieldsTemplates.setColumnType("text");
			initialGeneralFieldsTemplates.setControlType("dropdownlist");
			initialGeneralFieldsTemplates.setColumnFormat("");
			initialGeneralFieldsTemplates.setIsDynamic(false);
			initialGeneralFieldsTemplates.setIsMandatory(true);
			initialGeneralFieldsTemplates.setIsVisible(true);
			initialGeneralFieldsTemplates.setStatus(true);

			generalFieldsTemplatesRepository.save(initialGeneralFieldsTemplates);
		}

		initialGeneralFieldsTemplates = new GeneralFieldsTemplates();
		fieldsExists = generalFieldsTemplatesRepository.existsByTableIdAndColumnName(6, "resourceId");

		if (!fieldsExists) {

			initialGeneralFieldsTemplates.setTableId(6);
			initialGeneralFieldsTemplates.setColumnName("resourceId");
			initialGeneralFieldsTemplates.setFieldSeqNo(7);
			initialGeneralFieldsTemplates.setDisplayName("Assigned Resource");
			initialGeneralFieldsTemplates.setColumnType("text");
			initialGeneralFieldsTemplates.setControlType("dropdownlist");
			initialGeneralFieldsTemplates.setColumnFormat("");
			initialGeneralFieldsTemplates.setIsDynamic(false);
			initialGeneralFieldsTemplates.setIsMandatory(false);
			initialGeneralFieldsTemplates.setIsVisible(true);
			initialGeneralFieldsTemplates.setStatus(true);

			generalFieldsTemplatesRepository.save(initialGeneralFieldsTemplates);
		}

		initialGeneralFieldsTemplates = new GeneralFieldsTemplates();
		fieldsExists = generalFieldsTemplatesRepository.existsByTableIdAndColumnName(6, "startDateTime");

		if (!fieldsExists) {

			initialGeneralFieldsTemplates.setTableId(6);
			initialGeneralFieldsTemplates.setColumnName("startDateTime");
			initialGeneralFieldsTemplates.setFieldSeqNo(5);
			initialGeneralFieldsTemplates.setDisplayName("start Date Time");
			initialGeneralFieldsTemplates.setColumnType("date_time");
			initialGeneralFieldsTemplates.setControlType("calendar");
			initialGeneralFieldsTemplates.setColumnFormat("");
			initialGeneralFieldsTemplates.setIsDynamic(false);
			initialGeneralFieldsTemplates.setIsMandatory(true);
			initialGeneralFieldsTemplates.setIsVisible(true);
			initialGeneralFieldsTemplates.setStatus(true);

			generalFieldsTemplatesRepository.save(initialGeneralFieldsTemplates);
		}

		initialGeneralFieldsTemplates = new GeneralFieldsTemplates();
		fieldsExists = generalFieldsTemplatesRepository.existsByTableIdAndColumnName(6, "IsTimeWindowExists");

		if (!fieldsExists) {

			initialGeneralFieldsTemplates.setTableId(6);
			initialGeneralFieldsTemplates.setColumnName("IsTimeWindowExists");
			initialGeneralFieldsTemplates.setFieldSeqNo(4);
			initialGeneralFieldsTemplates.setDisplayName("TimeWindow");
			initialGeneralFieldsTemplates.setColumnType("text");
			initialGeneralFieldsTemplates.setControlType("button");
			initialGeneralFieldsTemplates.setColumnFormat("");
			initialGeneralFieldsTemplates.setIsDynamic(false);
			initialGeneralFieldsTemplates.setIsMandatory(false);
			initialGeneralFieldsTemplates.setIsVisible(true);
			initialGeneralFieldsTemplates.setStatus(true);

			generalFieldsTemplatesRepository.save(initialGeneralFieldsTemplates);
		}

		initialGeneralFieldsTemplates = new GeneralFieldsTemplates();
		fieldsExists = generalFieldsTemplatesRepository.existsByTableIdAndColumnName(7, "timeWindowId");

		if (!fieldsExists) {

			initialGeneralFieldsTemplates.setTableId(7);
			initialGeneralFieldsTemplates.setColumnName("timeWindowId");
			initialGeneralFieldsTemplates.setFieldSeqNo(3);
			initialGeneralFieldsTemplates.setDisplayName("Time Window Id");
			initialGeneralFieldsTemplates.setColumnType("text");
			initialGeneralFieldsTemplates.setControlType("dropdownlist");
			initialGeneralFieldsTemplates.setColumnFormat("");
			initialGeneralFieldsTemplates.setIsDynamic(false);
			initialGeneralFieldsTemplates.setIsMandatory(false);
			initialGeneralFieldsTemplates.setIsVisible(true);
			initialGeneralFieldsTemplates.setStatus(true);

			generalFieldsTemplatesRepository.save(initialGeneralFieldsTemplates);
		}

		initialGeneralFieldsTemplates = new GeneralFieldsTemplates();
		fieldsExists = generalFieldsTemplatesRepository.existsByTableIdAndColumnName(7, "taskId");

		if (!fieldsExists) {

			initialGeneralFieldsTemplates.setTableId(7);
			initialGeneralFieldsTemplates.setColumnName("taskId");
			initialGeneralFieldsTemplates.setFieldSeqNo(1);
			initialGeneralFieldsTemplates.setDisplayName("Task Id");
			initialGeneralFieldsTemplates.setColumnType("text");
			initialGeneralFieldsTemplates.setControlType("textbox");
			initialGeneralFieldsTemplates.setColumnFormat("");
			initialGeneralFieldsTemplates.setIsDynamic(false);
			initialGeneralFieldsTemplates.setIsMandatory(true);
			initialGeneralFieldsTemplates.setIsVisible(true);
			initialGeneralFieldsTemplates.setStatus(true);

			generalFieldsTemplatesRepository.save(initialGeneralFieldsTemplates);
		}

		initialGeneralFieldsTemplates = new GeneralFieldsTemplates();
		fieldsExists = generalFieldsTemplatesRepository.existsByTableIdAndColumnName(6, "taskName");

		if (!fieldsExists) {

			initialGeneralFieldsTemplates.setTableId(6);
			initialGeneralFieldsTemplates.setColumnName("taskName");
			initialGeneralFieldsTemplates.setFieldSeqNo(2);
			initialGeneralFieldsTemplates.setDisplayName("Task Name");
			initialGeneralFieldsTemplates.setColumnType("text");
			initialGeneralFieldsTemplates.setControlType("textbox");
			initialGeneralFieldsTemplates.setColumnFormat("");
			initialGeneralFieldsTemplates.setIsDynamic(false);
			initialGeneralFieldsTemplates.setIsMandatory(true);
			initialGeneralFieldsTemplates.setIsVisible(true);
			initialGeneralFieldsTemplates.setStatus(true);

			generalFieldsTemplatesRepository.save(initialGeneralFieldsTemplates);
		}

		initialGeneralFieldsTemplates = new GeneralFieldsTemplates();
		fieldsExists = generalFieldsTemplatesRepository.existsByTableIdAndColumnName(6, "taskId");

		if (!fieldsExists) {

			initialGeneralFieldsTemplates.setTableId(6);
			initialGeneralFieldsTemplates.setColumnName("taskId");
			initialGeneralFieldsTemplates.setFieldSeqNo(1);
			initialGeneralFieldsTemplates.setDisplayName("Task Id");
			initialGeneralFieldsTemplates.setColumnType("text");
			initialGeneralFieldsTemplates.setControlType("textbox");
			initialGeneralFieldsTemplates.setColumnFormat("");
			initialGeneralFieldsTemplates.setIsDynamic(false);
			initialGeneralFieldsTemplates.setIsMandatory(true);
			initialGeneralFieldsTemplates.setIsVisible(true);
			initialGeneralFieldsTemplates.setStatus(true);

			generalFieldsTemplatesRepository.save(initialGeneralFieldsTemplates);
		}

		initialGeneralFieldsTemplates = new GeneralFieldsTemplates();
		fieldsExists = generalFieldsTemplatesRepository.existsByTableIdAndColumnName(4, "defaultTaskDuration");

		if (!fieldsExists) {

			initialGeneralFieldsTemplates.setTableId(4);
			initialGeneralFieldsTemplates.setColumnName("defaultTaskDuration");
			initialGeneralFieldsTemplates.setFieldSeqNo(3);
			initialGeneralFieldsTemplates.setDisplayName("Duration(hr)");
			initialGeneralFieldsTemplates.setColumnType("numeric");
			initialGeneralFieldsTemplates.setControlType("textbox");
			initialGeneralFieldsTemplates.setColumnFormat("");
			initialGeneralFieldsTemplates.setIsDynamic(false);
			initialGeneralFieldsTemplates.setIsMandatory(true);
			initialGeneralFieldsTemplates.setIsVisible(true);
			initialGeneralFieldsTemplates.setStatus(true);

			generalFieldsTemplatesRepository.save(initialGeneralFieldsTemplates);
		}

		initialGeneralFieldsTemplates = new GeneralFieldsTemplates();
		fieldsExists = generalFieldsTemplatesRepository.existsByTableIdAndColumnName(4, "serviceName");

		if (!fieldsExists) {

			initialGeneralFieldsTemplates.setTableId(4);
			initialGeneralFieldsTemplates.setColumnName("serviceName");
			initialGeneralFieldsTemplates.setFieldSeqNo(2);
			initialGeneralFieldsTemplates.setDisplayName("Service Name");
			initialGeneralFieldsTemplates.setColumnType("text");
			initialGeneralFieldsTemplates.setControlType("textbox");
			initialGeneralFieldsTemplates.setColumnFormat("");
			initialGeneralFieldsTemplates.setIsDynamic(false);
			initialGeneralFieldsTemplates.setIsMandatory(true);
			initialGeneralFieldsTemplates.setIsVisible(true);
			initialGeneralFieldsTemplates.setStatus(true);

			generalFieldsTemplatesRepository.save(initialGeneralFieldsTemplates);
		}

		initialGeneralFieldsTemplates = new GeneralFieldsTemplates();
		fieldsExists = generalFieldsTemplatesRepository.existsByTableIdAndColumnName(4, "serviceId");

		if (!fieldsExists) {

			initialGeneralFieldsTemplates.setTableId(4);
			initialGeneralFieldsTemplates.setColumnName("serviceId");
			initialGeneralFieldsTemplates.setFieldSeqNo(1);
			initialGeneralFieldsTemplates.setDisplayName("Service ID");
			initialGeneralFieldsTemplates.setColumnType("text");
			initialGeneralFieldsTemplates.setControlType("textbox");
			initialGeneralFieldsTemplates.setColumnFormat("");
			initialGeneralFieldsTemplates.setIsDynamic(false);
			initialGeneralFieldsTemplates.setIsMandatory(true);
			initialGeneralFieldsTemplates.setIsVisible(true);
			initialGeneralFieldsTemplates.setStatus(true);

			generalFieldsTemplatesRepository.save(initialGeneralFieldsTemplates);
		}

		initialGeneralFieldsTemplates = new GeneralFieldsTemplates();
		fieldsExists = generalFieldsTemplatesRepository.existsByTableIdAndColumnName(5, "defaultTaskDuration");

		if (!fieldsExists) {

			initialGeneralFieldsTemplates.setTableId(5);
			initialGeneralFieldsTemplates.setColumnName("defaultTaskDuration");
			initialGeneralFieldsTemplates.setFieldSeqNo(5);
			initialGeneralFieldsTemplates.setDisplayName("Duration(hr)");
			initialGeneralFieldsTemplates.setColumnType("numeric");
			initialGeneralFieldsTemplates.setControlType("textbox");
			initialGeneralFieldsTemplates.setColumnFormat("");
			initialGeneralFieldsTemplates.setIsDynamic(false);
			initialGeneralFieldsTemplates.setIsMandatory(true);
			initialGeneralFieldsTemplates.setIsVisible(true);
			initialGeneralFieldsTemplates.setStatus(true);

			generalFieldsTemplatesRepository.save(initialGeneralFieldsTemplates);
		}

		initialGeneralFieldsTemplates = new GeneralFieldsTemplates();
		fieldsExists = generalFieldsTemplatesRepository.existsByTableIdAndColumnName(5, "serviceId");

		if (!fieldsExists) {

			initialGeneralFieldsTemplates.setTableId(5);
			initialGeneralFieldsTemplates.setColumnName("serviceId");
			initialGeneralFieldsTemplates.setFieldSeqNo(4);
			initialGeneralFieldsTemplates.setDisplayName("Service Id");
			initialGeneralFieldsTemplates.setColumnType("text");
			initialGeneralFieldsTemplates.setControlType("dropdownlist");
			initialGeneralFieldsTemplates.setColumnFormat("");
			initialGeneralFieldsTemplates.setIsDynamic(false);
			initialGeneralFieldsTemplates.setIsMandatory(true);
			initialGeneralFieldsTemplates.setIsVisible(true);
			initialGeneralFieldsTemplates.setStatus(true);

			generalFieldsTemplatesRepository.save(initialGeneralFieldsTemplates);
		}

		initialGeneralFieldsTemplates = new GeneralFieldsTemplates();
		fieldsExists = generalFieldsTemplatesRepository.existsByTableIdAndColumnName(5, "resourceId");

		if (!fieldsExists) {

			initialGeneralFieldsTemplates.setTableId(5);
			initialGeneralFieldsTemplates.setColumnName("resourceId");
			initialGeneralFieldsTemplates.setFieldSeqNo(3);
			initialGeneralFieldsTemplates.setDisplayName("Duty Officers");
			initialGeneralFieldsTemplates.setColumnType("text");
			initialGeneralFieldsTemplates.setControlType("dropdownlist");
			initialGeneralFieldsTemplates.setColumnFormat("");
			initialGeneralFieldsTemplates.setIsDynamic(false);
			initialGeneralFieldsTemplates.setIsMandatory(true);
			initialGeneralFieldsTemplates.setIsVisible(true);
			initialGeneralFieldsTemplates.setStatus(true);

			generalFieldsTemplatesRepository.save(initialGeneralFieldsTemplates);
		}

		initialGeneralFieldsTemplates = new GeneralFieldsTemplates();
		fieldsExists = generalFieldsTemplatesRepository.existsByTableIdAndColumnName(5, "skillName");

		if (!fieldsExists) {

			initialGeneralFieldsTemplates.setTableId(5);
			initialGeneralFieldsTemplates.setColumnName("skillName");
			initialGeneralFieldsTemplates.setFieldSeqNo(2);
			initialGeneralFieldsTemplates.setDisplayName("Skills Name");
			initialGeneralFieldsTemplates.setColumnType("text");
			initialGeneralFieldsTemplates.setControlType("textbox");
			initialGeneralFieldsTemplates.setColumnFormat("");
			initialGeneralFieldsTemplates.setIsDynamic(false);
			initialGeneralFieldsTemplates.setIsMandatory(true);
			initialGeneralFieldsTemplates.setIsVisible(true);
			initialGeneralFieldsTemplates.setStatus(true);

			generalFieldsTemplatesRepository.save(initialGeneralFieldsTemplates);
		}

		initialGeneralFieldsTemplates = new GeneralFieldsTemplates();
		fieldsExists = generalFieldsTemplatesRepository.existsByTableIdAndColumnName(5, "skillId");

		if (!fieldsExists) {

			initialGeneralFieldsTemplates.setTableId(5);
			initialGeneralFieldsTemplates.setColumnName("skillId");
			initialGeneralFieldsTemplates.setFieldSeqNo(1);
			initialGeneralFieldsTemplates.setDisplayName("Skills ID");
			initialGeneralFieldsTemplates.setColumnType("text");
			initialGeneralFieldsTemplates.setControlType("textbox");
			initialGeneralFieldsTemplates.setColumnFormat("");
			initialGeneralFieldsTemplates.setIsDynamic(false);
			initialGeneralFieldsTemplates.setIsMandatory(true);
			initialGeneralFieldsTemplates.setIsVisible(true);
			initialGeneralFieldsTemplates.setStatus(true);

			generalFieldsTemplatesRepository.save(initialGeneralFieldsTemplates);
		}

		initialGeneralFieldsTemplates = new GeneralFieldsTemplates();
		fieldsExists = generalFieldsTemplatesRepository.existsByTableIdAndColumnName(3, "timeWindowId");

		if (!fieldsExists) {

			initialGeneralFieldsTemplates.setTableId(3);
			initialGeneralFieldsTemplates.setColumnName("timeWindowId");
			initialGeneralFieldsTemplates.setFieldSeqNo(2);
			initialGeneralFieldsTemplates.setDisplayName("Time Window");
			initialGeneralFieldsTemplates.setColumnType("text");
			initialGeneralFieldsTemplates.setControlType("dropdownlist");
			initialGeneralFieldsTemplates.setColumnFormat("");
			initialGeneralFieldsTemplates.setIsDynamic(false);
			initialGeneralFieldsTemplates.setIsMandatory(true);
			initialGeneralFieldsTemplates.setIsVisible(true);
			initialGeneralFieldsTemplates.setStatus(true);

			generalFieldsTemplatesRepository.save(initialGeneralFieldsTemplates);
		}

		initialGeneralFieldsTemplates = new GeneralFieldsTemplates();
		fieldsExists = generalFieldsTemplatesRepository.existsByTableIdAndColumnName(3, "resourceId");

		if (!fieldsExists) {

			initialGeneralFieldsTemplates.setTableId(3);
			initialGeneralFieldsTemplates.setColumnName("resourceId");
			initialGeneralFieldsTemplates.setFieldSeqNo(1);
			initialGeneralFieldsTemplates.setDisplayName("Resource");
			initialGeneralFieldsTemplates.setColumnType("text");
			initialGeneralFieldsTemplates.setControlType("dropdownlist");
			initialGeneralFieldsTemplates.setColumnFormat("");
			initialGeneralFieldsTemplates.setIsDynamic(false);
			initialGeneralFieldsTemplates.setIsMandatory(true);
			initialGeneralFieldsTemplates.setIsVisible(true);
			initialGeneralFieldsTemplates.setStatus(true);

			generalFieldsTemplatesRepository.save(initialGeneralFieldsTemplates);
		}

		initialGeneralFieldsTemplates = new GeneralFieldsTemplates();
		fieldsExists = generalFieldsTemplatesRepository.existsByTableIdAndColumnName(2, "IsSkillsExists");

		if (!fieldsExists) {

			initialGeneralFieldsTemplates.setTableId(2);
			initialGeneralFieldsTemplates.setColumnName("IsSkillsExists");
			initialGeneralFieldsTemplates.setFieldSeqNo(4);
			initialGeneralFieldsTemplates.setDisplayName("View Capabilities");
			initialGeneralFieldsTemplates.setColumnType("text");
			initialGeneralFieldsTemplates.setControlType("button");
			initialGeneralFieldsTemplates.setColumnFormat("");
			initialGeneralFieldsTemplates.setIsDynamic(false);
			initialGeneralFieldsTemplates.setIsMandatory(false);
			initialGeneralFieldsTemplates.setIsVisible(true);
			initialGeneralFieldsTemplates.setStatus(true);

			generalFieldsTemplatesRepository.save(initialGeneralFieldsTemplates);
		}

		initialGeneralFieldsTemplates = new GeneralFieldsTemplates();
		fieldsExists = generalFieldsTemplatesRepository.existsByTableIdAndColumnName(2, "IsTimeWindowExists");

		if (!fieldsExists) {

			initialGeneralFieldsTemplates.setTableId(2);
			initialGeneralFieldsTemplates.setColumnName("IsTimeWindowExists");
			initialGeneralFieldsTemplates.setFieldSeqNo(3);
			initialGeneralFieldsTemplates.setDisplayName("View Availability");
			initialGeneralFieldsTemplates.setColumnType("text");
			initialGeneralFieldsTemplates.setControlType("button");
			initialGeneralFieldsTemplates.setColumnFormat("");
			initialGeneralFieldsTemplates.setIsDynamic(false);
			initialGeneralFieldsTemplates.setIsMandatory(false);
			initialGeneralFieldsTemplates.setIsVisible(true);
			initialGeneralFieldsTemplates.setStatus(true);

			generalFieldsTemplatesRepository.save(initialGeneralFieldsTemplates);
		}

		initialGeneralFieldsTemplates = new GeneralFieldsTemplates();
		fieldsExists = generalFieldsTemplatesRepository.existsByTableIdAndColumnName(2, "resourceName");

		if (!fieldsExists) {

			initialGeneralFieldsTemplates.setTableId(2);
			initialGeneralFieldsTemplates.setColumnName("resourceName");
			initialGeneralFieldsTemplates.setFieldSeqNo(2);
			initialGeneralFieldsTemplates.setDisplayName("Resource Name");
			initialGeneralFieldsTemplates.setColumnType("text");
			initialGeneralFieldsTemplates.setControlType("textbox");
			initialGeneralFieldsTemplates.setColumnFormat("");
			initialGeneralFieldsTemplates.setIsDynamic(false);
			initialGeneralFieldsTemplates.setIsMandatory(true);
			initialGeneralFieldsTemplates.setIsVisible(true);
			initialGeneralFieldsTemplates.setStatus(true);

			generalFieldsTemplatesRepository.save(initialGeneralFieldsTemplates);
		}

		initialGeneralFieldsTemplates = new GeneralFieldsTemplates();
		fieldsExists = generalFieldsTemplatesRepository.existsByTableIdAndColumnName(2, "resourceId");

		if (!fieldsExists) {

			initialGeneralFieldsTemplates.setTableId(2);
			initialGeneralFieldsTemplates.setColumnName("resourceId");
			initialGeneralFieldsTemplates.setFieldSeqNo(1);
			initialGeneralFieldsTemplates.setDisplayName("Resource Id");
			initialGeneralFieldsTemplates.setColumnType("text");
			initialGeneralFieldsTemplates.setControlType("textbox");
			initialGeneralFieldsTemplates.setColumnFormat("");
			initialGeneralFieldsTemplates.setIsDynamic(false);
			initialGeneralFieldsTemplates.setIsMandatory(true);
			initialGeneralFieldsTemplates.setIsVisible(true);
			initialGeneralFieldsTemplates.setStatus(true);

			generalFieldsTemplatesRepository.save(initialGeneralFieldsTemplates);
		}

		initialGeneralFieldsTemplates = new GeneralFieldsTemplates();
		fieldsExists = generalFieldsTemplatesRepository.existsByTableIdAndColumnName(1, "endDateTime");

		if (!fieldsExists) {

			initialGeneralFieldsTemplates.setTableId(1);
			initialGeneralFieldsTemplates.setColumnName("endDateTime");
			initialGeneralFieldsTemplates.setFieldSeqNo(4);
			initialGeneralFieldsTemplates.setDisplayName("End Date");
			initialGeneralFieldsTemplates.setColumnType("date_time");
			initialGeneralFieldsTemplates.setControlType("calendar");
			initialGeneralFieldsTemplates.setColumnFormat("DD MMM YYYY h:mm a");
			initialGeneralFieldsTemplates.setIsDynamic(false);
			initialGeneralFieldsTemplates.setIsMandatory(true);
			initialGeneralFieldsTemplates.setIsVisible(true);
			initialGeneralFieldsTemplates.setStatus(true);

			generalFieldsTemplatesRepository.save(initialGeneralFieldsTemplates);
		}

		initialGeneralFieldsTemplates = new GeneralFieldsTemplates();
		fieldsExists = generalFieldsTemplatesRepository.existsByTableIdAndColumnName(1, "startDateTime");

		if (!fieldsExists) {

			initialGeneralFieldsTemplates.setTableId(1);
			initialGeneralFieldsTemplates.setColumnName("startDateTime");
			initialGeneralFieldsTemplates.setFieldSeqNo(3);
			initialGeneralFieldsTemplates.setDisplayName("Start Date");
			initialGeneralFieldsTemplates.setColumnType("date_time");
			initialGeneralFieldsTemplates.setControlType("calendar");
			initialGeneralFieldsTemplates.setColumnFormat("DD MMM YYYY h:mm a");
			initialGeneralFieldsTemplates.setIsDynamic(false);
			initialGeneralFieldsTemplates.setIsMandatory(true);
			initialGeneralFieldsTemplates.setIsVisible(true);
			initialGeneralFieldsTemplates.setStatus(true);

			generalFieldsTemplatesRepository.save(initialGeneralFieldsTemplates);
		}

		initialGeneralFieldsTemplates = new GeneralFieldsTemplates();
		fieldsExists = generalFieldsTemplatesRepository.existsByTableIdAndColumnName(1, "timeWindowName");

		if (!fieldsExists) {

			initialGeneralFieldsTemplates.setTableId(1);
			initialGeneralFieldsTemplates.setColumnName("timeWindowName");
			initialGeneralFieldsTemplates.setFieldSeqNo(2);
			initialGeneralFieldsTemplates.setDisplayName("Shift Name");
			initialGeneralFieldsTemplates.setColumnType("text");
			initialGeneralFieldsTemplates.setControlType("textbox");
			initialGeneralFieldsTemplates.setColumnFormat("");
			initialGeneralFieldsTemplates.setIsDynamic(false);
			initialGeneralFieldsTemplates.setIsMandatory(false);
			initialGeneralFieldsTemplates.setIsVisible(true);
			initialGeneralFieldsTemplates.setStatus(true);

			generalFieldsTemplatesRepository.save(initialGeneralFieldsTemplates);
		}

		initialGeneralFieldsTemplates = new GeneralFieldsTemplates();
		fieldsExists = generalFieldsTemplatesRepository.existsByTableIdAndColumnName(1, "timeWindowId");

		if (!fieldsExists) {
			initialGeneralFieldsTemplates.setTableId(1);
			initialGeneralFieldsTemplates.setColumnName("timeWindowId");
			initialGeneralFieldsTemplates.setFieldSeqNo(1);
			initialGeneralFieldsTemplates.setDisplayName("TimeWindow Id");
			initialGeneralFieldsTemplates.setColumnType("text");
			initialGeneralFieldsTemplates.setControlType("textbox");
			initialGeneralFieldsTemplates.setColumnFormat("");
			initialGeneralFieldsTemplates.setIsDynamic(false);
			initialGeneralFieldsTemplates.setIsMandatory(true);
			initialGeneralFieldsTemplates.setIsVisible(true);
			initialGeneralFieldsTemplates.setStatus(true);

			generalFieldsTemplatesRepository.save(initialGeneralFieldsTemplates);
		}

	}

	private void seedTablesTemplateTable() {

		Boolean tableExists = generalTableTemplatesRepository.existsByTableName("timewindows");
		GeneralTablesTemplates initialGeneralTablesTemplates = new GeneralTablesTemplates();

		if (!tableExists) {

			initialGeneralTablesTemplates.setTableName("timewindows");
			initialGeneralTablesTemplates.setDisplayName("Roster Shift");
			initialGeneralTablesTemplates.setTableSeqNo(1);
			initialGeneralTablesTemplates.setIsEditable(true);
			initialGeneralTablesTemplates.setStatus(true);

			generalTableTemplatesRepository.save(initialGeneralTablesTemplates);
		}

		initialGeneralTablesTemplates = new GeneralTablesTemplates();
		tableExists = generalTableTemplatesRepository.existsByTableName("resources");

		if (!tableExists) {

			initialGeneralTablesTemplates.setTableName("resources");
			initialGeneralTablesTemplates.setDisplayName("Staff");
			initialGeneralTablesTemplates.setTableSeqNo(2);
			initialGeneralTablesTemplates.setIsEditable(true);
			initialGeneralTablesTemplates.setStatus(true);

			generalTableTemplatesRepository.save(initialGeneralTablesTemplates);
		}

		initialGeneralTablesTemplates = new GeneralTablesTemplates();
		tableExists = generalTableTemplatesRepository.existsByTableName("resources_timewindows");

		if (!tableExists) {

			initialGeneralTablesTemplates.setTableName("resources_timewindows");
			initialGeneralTablesTemplates.setDisplayName("Staff Time windows");
			initialGeneralTablesTemplates.setTableSeqNo(3);
			initialGeneralTablesTemplates.setIsEditable(true);
			initialGeneralTablesTemplates.setStatus(true);

			generalTableTemplatesRepository.save(initialGeneralTablesTemplates);
		}

		initialGeneralTablesTemplates = new GeneralTablesTemplates();
		tableExists = generalTableTemplatesRepository.existsByTableName("services");

		if (!tableExists) {

			initialGeneralTablesTemplates.setTableName("services");
			initialGeneralTablesTemplates.setDisplayName("Duty");
			initialGeneralTablesTemplates.setTableSeqNo(4);
			initialGeneralTablesTemplates.setIsEditable(true);
			initialGeneralTablesTemplates.setStatus(true);

			generalTableTemplatesRepository.save(initialGeneralTablesTemplates);
		}

		initialGeneralTablesTemplates = new GeneralTablesTemplates();
		tableExists = generalTableTemplatesRepository.existsByTableName("skills");

		if (!tableExists) {

			initialGeneralTablesTemplates.setTableName("skills");
			initialGeneralTablesTemplates.setDisplayName("Skills");
			initialGeneralTablesTemplates.setTableSeqNo(5);
			initialGeneralTablesTemplates.setIsEditable(true);
			initialGeneralTablesTemplates.setStatus(true);

			generalTableTemplatesRepository.save(initialGeneralTablesTemplates);
		}

		initialGeneralTablesTemplates = new GeneralTablesTemplates();
		tableExists = generalTableTemplatesRepository.existsByTableName("tasks");

		if (!tableExists) {

			initialGeneralTablesTemplates.setTableName("tasks");
			initialGeneralTablesTemplates.setDisplayName("Tasks");
			initialGeneralTablesTemplates.setTableSeqNo(6);
			initialGeneralTablesTemplates.setIsEditable(true);
			initialGeneralTablesTemplates.setStatus(true);

			generalTableTemplatesRepository.save(initialGeneralTablesTemplates);
		}

		initialGeneralTablesTemplates = new GeneralTablesTemplates();
		tableExists = generalTableTemplatesRepository.existsByTableName("tasks_timewindows");

		if (!tableExists) {

			initialGeneralTablesTemplates.setTableName("tasks_timewindows");
			initialGeneralTablesTemplates.setDisplayName("Task Time Windows");
			initialGeneralTablesTemplates.setTableSeqNo(7);
			initialGeneralTablesTemplates.setIsEditable(true);
			initialGeneralTablesTemplates.setStatus(true);

			generalTableTemplatesRepository.save(initialGeneralTablesTemplates);
		}

		initialGeneralTablesTemplates = new GeneralTablesTemplates();
		tableExists = generalTableTemplatesRepository.existsByTableName("location");

		if (!tableExists) {

			initialGeneralTablesTemplates.setTableName("location");
			initialGeneralTablesTemplates.setDisplayName("Locations");
			initialGeneralTablesTemplates.setTableSeqNo(8);
			initialGeneralTablesTemplates.setIsEditable(true);
			initialGeneralTablesTemplates.setStatus(true);

			generalTableTemplatesRepository.save(initialGeneralTablesTemplates);
		}

		initialGeneralTablesTemplates = new GeneralTablesTemplates();
		tableExists = generalTableTemplatesRepository.existsByTableName("resource_location");

		if (!tableExists) {

			initialGeneralTablesTemplates.setTableName("resource_location");
			initialGeneralTablesTemplates.setDisplayName("locations");
			initialGeneralTablesTemplates.setTableSeqNo(9);
			initialGeneralTablesTemplates.setIsEditable(true);
			initialGeneralTablesTemplates.setStatus(true);

			generalTableTemplatesRepository.save(initialGeneralTablesTemplates);
		}

		initialGeneralTablesTemplates = new GeneralTablesTemplates();
		tableExists = generalTableTemplatesRepository.existsByTableName("task_location");

		if (!tableExists) {

			initialGeneralTablesTemplates.setTableName("task_location");
			initialGeneralTablesTemplates.setDisplayName("locations");
			initialGeneralTablesTemplates.setTableSeqNo(10);
			initialGeneralTablesTemplates.setIsEditable(true);
			initialGeneralTablesTemplates.setStatus(true);

			generalTableTemplatesRepository.save(initialGeneralTablesTemplates);
		}

	}

	private void seedAdminUser() {

		Map<String, Object> user = new HashMap<>();
		List<Map<?, ?>> userList = new ArrayList();

		user.put("duty title", "Duty Title");
		user.put("email", "email@email.com");
		user.put("fullname", "Administrator");
		user.put("group", "general");
		user.put("name", "Admin");
		user.put("password", "P@ssw0rd123456");
		user.put("username", "admin");
		List userRole = new ArrayList<>();
		userRole.add("Module User");
		user.put("user role", userRole);

		userList.add(user);

		Map<String, List<Map<?, ?>>> userBody = new HashMap<>();

		userBody.put("userlist", userList);

		Boolean userExists = userRepository.existsByUsername("admin");

		if (!userExists)
			userController.save(userBody);

	}

}