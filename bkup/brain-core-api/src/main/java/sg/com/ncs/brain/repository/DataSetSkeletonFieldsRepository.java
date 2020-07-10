package sg.com.ncs.brain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.com.ncs.brain.entities.model.ModelDatasetSkeletonFields;

@Repository
public interface DataSetSkeletonFieldsRepository extends JpaRepository<ModelDatasetSkeletonFields, Integer> {

}
