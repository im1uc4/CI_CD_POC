package sg.com.ncs.brain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.com.ncs.brain.entities.model.GeneralTablesTemplates;

@Repository
public interface GeneralTableTemplatesRepository extends JpaRepository<GeneralTablesTemplates, Integer> {

	List<GeneralTablesTemplates> findAllByStatus(boolean b);

	Boolean existsByTableName(String string);
}
