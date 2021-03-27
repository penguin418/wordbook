package penguin.wordbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import penguin.wordbook.domain.QA;

public interface QARepository extends JpaRepository<QA, Long> {
}
