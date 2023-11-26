package penguin.wordbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import penguin.wordbook.domain.QA;

@Repository
public interface QARepository extends JpaRepository<QA, Long> {

}
