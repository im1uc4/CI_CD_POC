package sg.com.ncs.brain.services;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import sg.com.ncs.brain.entities.model.ConstraintType;
import sg.com.ncs.brain.entities.model.Constraints;

public interface ConstraintsService {

	Map<String, List<Map<String, Object>>> fetchConstraints(Integer id);

	List<Constraints> fetchAllConstraints();

	List<ConstraintType> saveConstraints(@Valid Map<?, ?> req);

}
