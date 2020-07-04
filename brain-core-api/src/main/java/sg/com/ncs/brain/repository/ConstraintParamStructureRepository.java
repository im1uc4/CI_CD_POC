package sg.com.ncs.brain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.com.ncs.brain.entities.model.ConstraintParamStructure;

@Repository
public interface ConstraintParamStructureRepository extends JpaRepository<ConstraintParamStructure, Integer> {

	List<ConstraintParamStructure> findByConstraintParamId(Integer id);

	boolean existsByConstraintParamIdAndKey(Integer id, Object parameterKey);
}
