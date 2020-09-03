package sg.com.ncs.brain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.com.ncs.brain.entities.model.ConstraintParamValue;

@Repository
public interface ConstraintsParamValueRepository extends JpaRepository<ConstraintParamValue, Integer> {

}
