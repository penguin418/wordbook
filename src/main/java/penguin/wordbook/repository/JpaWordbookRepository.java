package penguin.wordbook.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import penguin.wordbook.domain.Wordbook;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaWordbookRepository implements WordbookRepository{

    private final EntityManager em;

    @Autowired
    public JpaWordbookRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Wordbook save(Wordbook wordbook) {
        em.persist(wordbook);
        return wordbook;
    }

    @Override
    public Optional<Wordbook> findById(Long id){
        return em.createQuery("select w from wordbook w where w.id = :id", Wordbook.class)
                .setParameter("id", id)
                .getResultList().stream().findAny();
    }

    @Override
    public List<Wordbook> findAll(){
        return em.createQuery("select w from wordbook w", Wordbook.class).getResultList();
    }
    @Override
    public boolean delete(Wordbook wordbook) {
        int resultCount = em.createQuery("delete from wordbook w where w.id=:id", Wordbook.class)
                .setParameter("id", wordbook.getId())
                .executeUpdate();
        return resultCount == 1;
    }
}
