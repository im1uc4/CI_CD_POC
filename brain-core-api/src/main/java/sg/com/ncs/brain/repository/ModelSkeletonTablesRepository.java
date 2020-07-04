package sg.com.ncs.brain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sg.com.ncs.brain.entities.model.ModelDatasetSkeletonTables;

@Repository
public interface ModelSkeletonTablesRepository extends JpaRepository<ModelDatasetSkeletonTables, Integer> {

	Boolean existsByModelIdAndTableNameAndStatus(Integer id, String table, boolean b);

	ModelDatasetSkeletonTables findByModelIdAndTableNameAndStatus(Integer id, String table, boolean b);

	List<ModelDatasetSkeletonTables> findByModelIdAndStatus(Integer id, boolean b);

	@Query(value = "SELECT \r\n" + "    mdsf.id,\r\n" + "    ds.model_id,\r\n" + "    ds.id 'dataset_id',\r\n"
			+ "    mdst.table_name,\r\n" + "    mdst.display_name 'table_display_name',\r\n"
			+ "    mdsf.column_name columnName,\r\n" + "    mdsf.fieldSeqNo 'fieldSeqNo',\r\n"
			+ "    mdsf.display_name displayName,\r\n" + "    mdsf.column_type,\r\n" + "    mdsf.control_type,\r\n"
			+ "    mdsf.column_format,\r\n" + "    mdst.is_editable,\r\n" + "    mdsf.is_mandatory,\r\n"
			+ "    mdsf.is_visible,\r\n" + "    mdsf.status,\r\n" + "    mdsf.created_by,\r\n"
			+ "    mdsf.updated_by,\r\n" + "    mdsf.createdAt 'createdAt',\r\n" + "    mdsf.updatedAt 'updatedAt'\r\n"
			+ "FROM\r\n" + "    models mods\r\n" + "        INNER JOIN\r\n"
			+ "    datasets ds ON mods.id = ds.model_id\r\n" + "        INNER JOIN\r\n"
			+ "    model_dataset_skeleton_tables mdst ON mods.id = mdst.model_id\r\n" + "        INNER JOIN\r\n"
			+ "    model_dataset_skeleton_fields mdsf ON mdst.id = mdsf.table_id\r\n" + "WHERE\r\n"
			+ "    ds.id = :datasetId\r\n" + "ORDER BY ds.id , mdst.tableSeqNo , mdsf.fieldSeqNo", nativeQuery = true)
	List<Object[]> findSkeletonByDsId(@Param("datasetId") Integer datasetId);

}
