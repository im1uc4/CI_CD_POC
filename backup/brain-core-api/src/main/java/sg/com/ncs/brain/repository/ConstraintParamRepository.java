package sg.com.ncs.brain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.com.ncs.brain.entities.model.ConstraintParam;

@Repository
public interface ConstraintParamRepository extends JpaRepository<ConstraintParam, Integer> {

	ConstraintParam findByParameterIdAndConstraintTypeId(String parameterId, Integer id);

	boolean existsByParameterIdAndConstraintTypeId(String parameterId, Integer id);
}
