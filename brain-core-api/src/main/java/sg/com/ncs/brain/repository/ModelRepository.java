package sg.com.ncs.brain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.com.ncs.brain.entities.model.Model;

@Repository
public interface ModelRepository extends JpaRepository<Model, Integer> {

	Boolean existsByOptimisationModelIdAndModelStatus(String optModelId, boolean b);

	Model findByOptimisationModelIdAndModelStatus(String optModelId, boolean b);

	Boolean existsByOptimisationModelId(String optimisationModelId);

}
