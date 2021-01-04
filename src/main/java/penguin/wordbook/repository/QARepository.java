package penguin.wordbook.repository;

import penguin.wordbook.domain.QA;

import java.util.List;
import java.util.Optional;

public interface QARepository {
    QA save(QA qa);
    List<QA> findAll();
    boolean delete(QA qa);
}
