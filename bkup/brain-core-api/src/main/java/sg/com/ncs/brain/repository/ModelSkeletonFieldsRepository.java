package sg.com.ncs.brain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.com.ncs.brain.entities.model.ModelDatasetSkeletonFields;

@Repository
public interface ModelSkeletonFieldsRepository extends JpaRepository<ModelDatasetSkeletonFields, Integer> {

	Boolean existsByTableIdAndColumnName(Integer id, String columnName);

	ModelDatasetSkeletonFields findTopByTableIdOrderByFieldSeqNoDesc(Integer id);

	List<ModelDatasetSkeletonFields> findByStatus(boolean b);

}
