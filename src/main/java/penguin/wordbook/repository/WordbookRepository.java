package penguin.wordbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import penguin.wordbook.domain.Wordbook;

import java.util.List;

@Repository
public interface WordbookRepository extends JpaRepository<Wordbook, Long> {
}
