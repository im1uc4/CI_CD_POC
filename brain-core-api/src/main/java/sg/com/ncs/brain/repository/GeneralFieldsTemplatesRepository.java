package sg.com.ncs.brain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.com.ncs.brain.entities.model.GeneralFieldsTemplates;

@Repository
public interface GeneralFieldsTemplatesRepository extends JpaRepository<GeneralFieldsTemplates, Integer> {

	List<GeneralFieldsTemplates> findAllByStatus(boolean b);

	List<GeneralFieldsTemplates> findAllByTableIdAndStatus(Integer id, boolean b);

	Boolean existsByTableIdAndColumnName(Integer tableId, String ColumnName);

}
