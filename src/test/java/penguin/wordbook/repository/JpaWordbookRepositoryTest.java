package penguin.wordbook.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import penguin.wordbook.domain.QA;
import penguin.wordbook.domain.Wordbook;
import penguin.wordbook.service.WordbookService;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
class JpaWordbookRepositoryTest {

    @Autowired
    WordbookService wordbookService;
    @Qualifier("jpaWordbookRepository")
    @Autowired
    WordbookRepository wordbookRepository;

    @Autowired
    QARepository qaRepository;

    @Test
    void save() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        // given
        Wordbook wb1 = new Wordbook();
        wb1.setQaList(new ArrayList<>());
        // when
//        wb1.getQaList().add(new QA("q1", "a1"));
        System.out.println(objectMapper.writeValueAsString(wb1));
        wordbookService.create(wb1);

        Wordbook wb2 = new Wordbook();
        wb2.setQaList(new ArrayList<>());
//        wb2.getQaList().add(new QA("q2", "a2"));
        System.out.println(objectMapper.writeValueAsString(wb2));
        wordbookService.create(wb2);

        // then
        List<Wordbook> wordbookList = wordbookService.findAll();
        for(Wordbook wb:wordbookList){
            System.out.println(objectMapper.writeValueAsString(wb));
        }

        List<QA> qaList = qaRepository.findAll();
        for(QA qa:qaList){
            System.out.println(objectMapper.writeValueAsString(qa));
        }
        System.out.println("END");



    }

    @Test
    void delete() {
    }
}