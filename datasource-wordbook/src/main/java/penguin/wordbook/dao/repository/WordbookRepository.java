package penguin.wordbook.dao.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import penguin.wordbook.model.entity.Wordbook;

import java.util.List;

@Repository
public interface WordbookRepository extends JpaRepository<Wordbook, Long> {
    public List<Wordbook> findByAccountId(Long accountId);

    @EntityGraph(attributePaths = {"qaDetail.qaList"}, type = EntityGraph.EntityGraphType.FETCH)
    public Wordbook findWithQaByWordbookId(Long wordbookId);
}
