package penguin.wordbook.repository;

import org.springframework.stereotype.Repository;
import penguin.wordbook.domain.Wordbook;

import java.util.List;
import java.util.Optional;

@Repository
public interface WordbookRepository {
    Wordbook save(Wordbook wordbook);
    Optional<Wordbook> findById(Long id);
    List<Wordbook> findAll();
    boolean delete(Wordbook wordbook);
}
