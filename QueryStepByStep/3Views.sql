drop table group_model_access;
drop table user_model_access;


DROP VIEW IF EXISTS `group_model_access`;
CREATE  VIEW `group_model_access` AS select `mdl`.`id` AS `id`,`acc_t`.`access_type` AS `access_type`,`mdl`.`model_name` AS `model_name`,`mdl`.`model_description_header` AS `model_description_header`,`mdl`.`model_description_details` AS `model_description_details`,`mc`.`custom_field_value` AS `person_in_charge`,`usr`.`username` AS `username`,`mdl`.`created_by` AS `created_by` from ((((((`access_types` `acc_t` join `model_accesses` `m_acc` on((`m_acc`.`access_type_id` = `acc_t`.`id`))) join `groups` `grp` on((`m_acc`.`target_id` = `grp`.`id`))) join `user_groups` `usr_grp` on((`usr_grp`.`groupsId` = `grp`.`id`))) join `users` `usr` on((`usr_grp`.`usersId` = `usr`.`id`))) join `models` `mdl` on((`mdl`.`id` = `m_acc`.`model_id`))) left join `model_customfields` `mc` on(((`mdl`.`id` = `mc`.`model_id`) and (`mc`.`custom_field_name` = 'person_in_charge')))) where ((`acc_t`.`access_type` = 'group') and (`acc_t`.`status` = true) and (`m_acc`.`status` = true) and (`usr`.`status` = true) and (`mdl`.`model_status` = true)) ;



 DROP VIEW IF EXISTS `user_model_access`;

CREATE  VIEW `user_model_access` AS select `mdl`.`id` AS `id`,`acc_t`.`access_type` AS `access_type`,`mdl`.`model_name` AS `model_name`,`mdl`.`model_description_header` AS `model_description_header`,`mdl`.`model_description_details` AS `model_description_details`,`mc`.`custom_field_value` AS `person_in_charge`,`usr`.`username` AS `username`,`mdl`.`created_by` AS `created_by` from ((((`access_types` `acc_t` join `model_accesses` `m_acc` on((`m_acc`.`access_type_id` = `acc_t`.`id`))) join `users` `usr` on((`m_acc`.`target_id` = `usr`.`id`))) join `models` `mdl` on((`mdl`.`id` = `m_acc`.`model_id`))) left join `model_customfields` `mc` on(((`mdl`.`id` = `mc`.`model_id`) and (`mc`.`custom_field_name` = 'person_in_charge')))) where ((`acc_t`.`access_type` = 'user') and (`acc_t`.`status` = true) and (`m_acc`.`status` = true) and (`usr`.`status` = true) and (`mdl`.`model_status` = true));