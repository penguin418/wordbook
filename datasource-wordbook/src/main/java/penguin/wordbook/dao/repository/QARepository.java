package penguin.wordbook.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import penguin.wordbook.model.entity.QA;

@Repository
public interface QARepository extends JpaRepository<QA, Long> {

}
