package sg.com.ncs.brain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.com.ncs.brain.entities.model.ConstraintType;

@Repository
public interface ConstraintTypeRepository extends JpaRepository<ConstraintType, Integer> {

	ConstraintType findByConstraintTypeId(String constraintTypeId);

	boolean existsByConstraintTypeId(String constraintTypeId);
}
