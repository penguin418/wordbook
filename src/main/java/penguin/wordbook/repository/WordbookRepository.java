package penguin.wordbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import penguin.wordbook.domain.Wordbook;

public interface WordbookRepository extends JpaRepository<Wordbook, Long> {
}
