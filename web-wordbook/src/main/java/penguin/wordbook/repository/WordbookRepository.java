package penguin.wordbook.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import penguin.wordbook.domain.Account;
import penguin.wordbook.domain.Wordbook;

import java.util.List;

@Repository
public interface WordbookRepository extends JpaRepository<Wordbook, Long> {
    public List<Wordbook> findByAccountAccountId(Long accountId);

    @EntityGraph(attributePaths = {"qaDetail.qaList"}, type = EntityGraph.EntityGraphType.FETCH)
    public Wordbook findWithQaByWordbookId(Long wordbookId);
}
