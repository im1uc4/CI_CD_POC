package sg.com.ncs.brain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.com.ncs.brain.entities.model.AccessType;
import sg.com.ncs.brain.entities.model.ModelAccess;

@Repository
public interface ModelAccessRepository extends JpaRepository<ModelAccess, Integer> {

	Boolean existsByModelIdAndAccessTypeIdAndTargetId(Integer modelId, Integer AccessTypeId, Integer targetId);

	Boolean existsByModelIdAndAccessTypeIdAndTargetId(Integer modelId, AccessType AccessTypeId, Integer targetId);

	Boolean existsByModelIdAndStatus(Integer modelId, Boolean status);

	ModelAccess findByModelIdAndAccessTypeIdAndTargetId(Integer id, Integer id2, Integer targetId);

}
