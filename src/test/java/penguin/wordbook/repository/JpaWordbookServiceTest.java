package penguin.wordbook.repository;

import static penguin.wordbook.controller.dto.WordbookDto.*;
import static penguin.wordbook.controller.dto.QADto.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import penguin.wordbook.service.WordbookService;

import java.util.List;

@SpringBootTest
@Transactional
class JpaWordbookServiceTest {

    @Autowired
    WordbookService wordbookService;

    @Test
    @Transactional
    void save() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        // given
        WordbookCreateDto wb1 = new WordbookCreateDto("first wordbook", "this is for test");
        // when
        wb1.getQaList().add(new QACreateDto("new q", "new a"));

        System.out.println(objectMapper.writeValueAsString(wb1));
        wordbookService.create(wb1);

        // then
        List<WordbookItemDto> dtos = wordbookService.findAll();
        for(WordbookItemDto wb:dtos){
            System.out.println(objectMapper.writeValueAsString(wb));
        }
    }

    @Test
    void delete() {
        // given
        List<WordbookItemDto> dtos = wordbookService.findAll();
        System.out.println(dtos);

        // when
        WordbookItemDto wb = dtos.get(0);
        Long id = wb.getId();
        wordbookService.removeById(id);

        // then
        List<WordbookItemDto> dtos2 = wordbookService.findAll();
        System.out.println(dtos2);
    }
}