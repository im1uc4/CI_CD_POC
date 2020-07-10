package sg.com.ncs.brain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sg.com.ncs.brain.model.dataset.Datasets;

@Repository
public interface DatasetRepository extends JpaRepository<Datasets, Integer> {

	Integer countByDatasetIdAndNameAndIsMasterdsAndStatus(String datasetId, String datasetName, Boolean isMasterDataset,
			boolean b);

	Boolean existsByDatasetIdAndNameAndIsMasterdsAndStatus(String datasetId, String datasetName,
			Boolean isMasterDataset, boolean b);

	List<Datasets> findByDatasetIdAndStatus(String datasetId, boolean b);

	@Query(value = "SELECT \r\n" + "    ds.id 'id',\r\n" + "    ds.model_id 'model_id',\r\n"
			+ "    ds.id 'dataset_id',\r\n" + "    ds.name 'dataset_name',\r\n"
			+ "    ds.is_masterds 'isMasterdataset',\r\n" + "    ds.description 'dataset_description',\r\n"
			+ "    ds.scheduleDateTime 'dataset_scheduledatetime',\r\n" + "    usr.name 'created_by',\r\n"
			+ "    ds.updated_by 'updated_by',\r\n" + "    ds.createdAt 'createdAt',\r\n"
			+ "    ds.updatedAt 'updatedAt'\r\n" + "FROM\r\n" + "    datasets ds\r\n" + "        INNER JOIN\r\n"
			+ "    users usr ON ds.created_by = usr.username\r\n" + "WHERE\r\n"
			+ "    ds.model_id = :modelId AND ds.status = TRUE", nativeQuery = true)
	List<Object[]> findByDsModelId(@Param("modelId") Integer modelId);

	@Query(value = "SELECT \r\n" + "    dd.id,\r\n" + "    record_id,\r\n" + "    mdst.table_name,\r\n"
			+ "    mdst.display_name 'table_display_name',\r\n" + "    dataset_id,\r\n"
			+ "    mdsf.column_name 'field_name',\r\n" + "    value,\r\n" + "    dd.status,\r\n"
			+ "    dd.created_by,\r\n" + "    dd.updated_by,\r\n" + "    dd.createdAt,\r\n" + "    dd.updatedAt\r\n"
			+ "FROM\r\n" + "    optimisation.dataset_data dd\r\n" + "        INNER JOIN\r\n"
			+ "    optimisation.model_dataset_skeleton_fields mdsf ON dd.dataset_skeleton_field_id = mdsf.id\r\n"
			+ "        INNER JOIN\r\n"
			+ "    optimisation.model_dataset_skeleton_tables mdst ON mdst.id = mdsf.table_id\r\n" + "WHERE\r\n"
			+ "    dataset_id = :datasetId \r\n" + "ORDER BY mdst.tableSeqNo , mdsf.fieldSeqNo;", nativeQuery = true)
	List<Object[]> findRawDs(@Param("datasetId") Integer datasetId);

}
