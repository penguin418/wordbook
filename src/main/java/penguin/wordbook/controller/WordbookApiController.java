package penguin.wordbook.controller;

import static penguin.wordbook.controller.dto.WordbookDto.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import penguin.wordbook.service.WordbookService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static java.lang.Long.parseLong;

@RestController
@AllArgsConstructor
public class WordbookApiController {
    private final WordbookService wordbookService;

    /**
     * 새로운 wordbook 생성
     *
     * @param wordbook {WordbookCreateDto}
     * @return ResponseEntity 성공 시 WordbookResponseDto, 실패 시 badRequest
     * @throws JsonProcessingException
     */
    @PostMapping("/api/wordbooks")
    public ResponseEntity<WordbookDetailDto> PostWordbookCreate(@RequestBody WordbookCreateDto wordbook) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(wordbook));
        try {
            WordbookDetailDto dto = wordbookService.create(wordbook);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 전체 wordbook 가져오기
     *
     * @return ResponseEntity
     */
    @GetMapping("/api/wordbooks")
    public ResponseEntity<WordbooksResultSetDto> GetWordbookList() {
        try {
            List<WordbookItemDto> results = wordbookService.findAll();
            WordbooksResultSetDto resultSet = new WordbooksResultSetDto((long) results.size(), 1L, results);
            return ResponseEntity.ok(resultSet);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 단일 wordbook 가져오기
     *
     * @param id {Long}
     * @return
     */
    @GetMapping("/api/wordbooks/{id}")
    public ResponseEntity<WordbookDetailDto> GetWordbookUpdate(@PathVariable(value = "id") String id) {
        try {
            Long wordbookId = Long.parseLong(id);
            WordbookDetailDto dto = wordbookService.findOne(wordbookId);
            return ResponseEntity.ok(dto);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * wordbook 갱신
     *
     * @param id       {String}
     * @param wordbook {WordbookUpdateDto}
     * @return ResponseEntity
     * @throws JsonProcessingException
     */
    @PostMapping("/api/wordbooks/{id}")
    public ResponseEntity<?> PostWordbookUpdate(@PathVariable(value = "id") String id, @RequestBody WordbookUpdateDto wordbook) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(wordbook));
        wordbookService.update(wordbook);
        return ResponseEntity.ok().build();
    }

    /**
     * wordbook 삭제
     *
     * @param id {String} wordbook id (숫자)
     * @return ResponseEntity
     */
    @DeleteMapping("/api/wordbooks/{id}")
    @ResponseBody
    public ResponseEntity<?> DeleteWordbook(@PathVariable(value = "id") String id) {
        try {
            Long wordbookId = Long.parseLong(id);
            wordbookService.removeById(wordbookId);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


}
