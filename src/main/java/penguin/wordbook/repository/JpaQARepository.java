package penguin.wordbook.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import penguin.wordbook.domain.QA;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class JpaQARepository implements QARepository{
    private final EntityManager em;
    @Autowired
    public JpaQARepository(EntityManager em){this.em = em;}

    @Override
    public QA save(QA qa) {
        System.out.println("this is QA writing");
        em.persist(qa);
        return qa;
    }

    @Override
    public List<QA> findAll(){
        return em.createQuery("select q from qa q", QA.class).getResultList();
    }

    @Override
    public boolean delete(QA qa) {
        int resultCount = em.createQuery("delete from qa q where q.id=:id")
                .setParameter("id", qa.getId())
                .executeUpdate();
        return resultCount == 1;
    }
}
