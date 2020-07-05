

INSERT INTO `access_types`(
id,
access_type,
status,
created_by,
updated_by,
createdAt,
updatedAt)
 VALUES (1,'user',1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(2,'group',1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05');



INSERT INTO `general_fields_templates`
(
id,
table_id,
column_name, 
fieldSeqNo,
display_name,
column_type,
control_type,
column_format,
is_dynamic,
is_mandatory,
is_visible,
status,
created_by,
updated_by,
createdAt,
updatedAt
)
VALUES (1,1,'timeWindowId',1,'TimeWindow Id','text','textbox','',0,1,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(2,1,'timeWindowName',2,'Shift Name','text','textbox','',0,0,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(3,1,'startDateTime',3,'Start Date','date_time','calendar','DD MMM YYYY h:mm a',0,1,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(4,1,'endDateTime',4,'End Date','date_time','calendar','DD MMM YYYY h:mm a',0,1,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(5,2,'resourceId',1,'Resource Id','text','textbox','',0,1,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(6,2,'resourceName',2,'Resource Name','text','textbox','',0,1,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(7,2,'IsTimeWindowExists',3,'View Availability','text','button','',0,0,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(8,2,'IsSkillsExists',4,'View Capabilities','text','button','',0,0,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(9,3,'resourceId',1,'Resource','text','dropdownlist','',0,1,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(10,3,'timeWindowId',2,'Time Window','text','dropdownlist','',0,1,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(11,5,'skillId',1,'Skills ID','text','textbox','',0,1,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(12,5,'skillName',2,'Skills Name','text','textbox','',0,1,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(13,5,'resourceId',3,'Duty Officers','text','dropdownlist','',0,1,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(14,5,'serviceId',4,'Service Id','text','dropdownlist','',0,1,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(15,5,'defaultTaskDuration',5,'Duration(hr)','numeric','textbox','',0,1,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(16,4,'serviceId',1,'Service ID','text','textbox','',0,1,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(17,4,'serviceName',2,'Service Name','text','textbox','',0,1,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(18,4,'defaultTaskDuration',3,'Duration(hr)','numeric','textbox','',0,1,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(19,6,'taskId',1,'Task Id','text','textbox','',0,1,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(20,6,'taskName',2,'Task Name','text','textbox','',0,1,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(21,7,'taskId',1,'Task Id','text','textbox','',0,1,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(22,7,'timeWindowId',3,'Time Window Id','text','dropdownlist','',0,0,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(23,6,'IsTimeWindowExists',4,'TimeWindow','text','button','',0,0,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(24,6,'startDateTime',5,'start Date Time','date_time','calendar','',0,1,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(25,6,'resourceId',7,'Assigned Resource','text','dropdownlist','',0,0,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(26,6,'serviceId',8,'Service Required','text','dropdownlist','',0,1,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(27,6,'taskDuration',9,'Task Duration','numeric','textbox','',0,1,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(28,6,'resourceLocked',10,'Resource Locked','numeric','checkbox','',0,1,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(29,6,'startDateTimeLocked',111,'Start Date Time Locked','numeric','checkbox','',0,1,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05');


INSERT INTO `general_tables_templates`
(
id, 
table_name,
display_name,
tableSeqNo,
is_editable,
status,
created_by, 
updated_by, 
createdAt,
updatedAt
)
VALUES (1,'timewindows','Roster Shift',1,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(2,'resources','Staff',2,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(3,'resources_timewindows','Staff Time windows',3,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(4,'services','Duty',4,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(5,'skills','Skills',5,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(6,'tasks','Tasks',6,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(7,'tasks_timewindows','Task Time Windows',7,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(8,'location','Locations',8,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(9,'resource_location','locations',9,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05'),(10,'task_location','locations',10,1,1,'System','System','2019-12-23 13:13:05','2019-12-23 13:13:05');






