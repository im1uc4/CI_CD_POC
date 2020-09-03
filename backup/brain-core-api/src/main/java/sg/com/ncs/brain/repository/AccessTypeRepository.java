package sg.com.ncs.brain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.com.ncs.brain.entities.model.AccessType;
import sg.com.ncs.brain.entities.model.ModelAccess;

@Repository
public interface AccessTypeRepository extends JpaRepository<AccessType, Integer> {

	AccessType findByAccessTypeAndStatus(String accessType, boolean b);

	AccessType findByAccessType(String accessType);

	Boolean existsByAccessType(String accessType);

}