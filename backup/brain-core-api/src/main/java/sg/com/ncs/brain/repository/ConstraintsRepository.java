package sg.com.ncs.brain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.com.ncs.brain.entities.model.Constraints;

@Repository
public interface ConstraintsRepository extends JpaRepository<Constraints, Integer> {

	List<Constraints> findAllById(Integer id);

	List<Constraints> findAllByModelId(Integer model_id);

}
