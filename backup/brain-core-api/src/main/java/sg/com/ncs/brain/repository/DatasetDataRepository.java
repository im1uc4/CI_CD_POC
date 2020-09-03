package sg.com.ncs.brain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sg.com.ncs.brain.model.dataset.DatasetData;

@Repository
public interface DatasetDataRepository extends JpaRepository<DatasetData, Integer> {

	// after testing make ='button' to !=button

	@Query(value = "SELECT \r\n" + 
			"    dd.record_id,\r\n" + 
			"    mdst.table_name,\r\n" + 
			"    mdsf.column_name,\r\n" + 
			"    CASE\r\n" + 
			"        WHEN dd.value = '' THEN NULL\r\n" + 
			"        ELSE dd.value\r\n" + 
			"    END AS 'value',\r\n" + 
			"    dd.id,\r\n" + 
			"    dd.dataset_id,\r\n" + 
			"    dd.dataset_skeleton_table_id,\r\n" + 
			"    mdsf.table_id,\r\n" + 
			"    dd.dataset_skeleton_field_id,\r\n" + 
			"    mdsf.column_type,\r\n" + 
			"    mdsf.is_dynamic,\r\n" + 
			"    dd.status\r\n" + 
			" FROM\r\n" + 
			"    dataset_data dd\r\n" + 
			"        INNER JOIN\r\n" + 
			"    model_dataset_skeleton_tables mdst ON dd.dataset_skeleton_table_id = mdst.id\r\n" + 
			"        INNER JOIN\r\n" + 
			"    model_dataset_skeleton_fields mdsf ON dd.dataset_skeleton_field_id = mdsf.id\r\n" + 
			" WHERE\r\n" + 
			"    dd.dataset_id = :datasetId \r\n" + 
			"        AND mdsf.control_type != 'button'", nativeQuery = true)
	List<Object[]> findDataset(@Param("datasetId") Integer datasetId);

	@Query(value = "SELECT DISTINCT\r\n" + "    mdsf.column_name,\r\n" + "    mdsf.column_type,\r\n"
			+ "    mdsf.control_type,\r\n" + "    mdsf.is_dynamic,\r\n" + "    mdst.table_name\r\n" + "FROM\r\n"
			+ "    dataset_data dd\r\n" + "        INNER JOIN\r\n"
			+ "    model_dataset_skeleton_tables mdst ON dd.dataset_skeleton_table_id = mdst.id\r\n"
			+ "        INNER JOIN\r\n"
			+ "    model_dataset_skeleton_fields mdsf ON dd.dataset_skeleton_field_id = mdsf.id\r\n" + "WHERE\r\n"
			+ "    dd.dataset_id = :datasetId", nativeQuery = true)
	List<Object[]> findDynamicFields(@Param("datasetId") Integer datasetId);

	@Query(value = "SELECT \r\n" + 
			"    record_id,\r\n" + 
			"    mdst.table_name,\r\n" + 
			"    mdsf.column_name 'field_name',\r\n" + 
			"    value,\r\n" + 
			"    dd.id,\r\n" + 
			"    mdst.display_name 'table_display_name',\r\n" + 
			"    dataset_id 'dataset_id',\r\n" + 
			"    dd.status,\r\n" + 
			"    dd.created_by,\r\n" + 
			"    dd.updated_by,\r\n" + 
			"    dd.createdAt 'createdAt',\r\n" + 
			"    dd.updatedAt 'updatedAt'\r\n" + 
			" FROM\r\n" + 
			"    dataset_data dd\r\n" + 
			"        INNER JOIN\r\n" + 
			"    model_dataset_skeleton_fields mdsf ON dd.dataset_skeleton_field_id = mdsf.id\r\n" + 
			"        INNER JOIN\r\n" + 
			"    model_dataset_skeleton_tables mdst ON mdst.id = mdsf.table_id\r\n" + 
			"	 WHERE  dataset_id = :datasetId AND mdst.id IN (:resourceTabId,:serviceTabId) \r\n" + 
			"	 ORDER BY mdst.tableSeqNo , mdsf.fieldSeqNo", nativeQuery = true)
	List<Object[]> findDataSetData(@Param("datasetId") Integer datasetId, @Param("resourceTabId") Integer resourceTabId,
			@Param("serviceTabId") Integer serviceTabId);

}
