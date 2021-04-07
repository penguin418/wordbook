package penguin.wordbook.controller;

import static penguin.wordbook.controller.dto.WordbookDto.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
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
        WordbookDetailDto dto = wordbookService.create(wordbook);
        return ResponseEntity.ok(dto);
    }

    /**
     * 전체 wordbook 가져오기
     *
     * @return ResponseEntity
     */
    @GetMapping("/api/wordbooks")
    public ResponseEntity<WordbooksResultSetDto> GetWordbookList() {
        List<WordbookItemDto> results = wordbookService.findAll();
        WordbooksResultSetDto resultSet = new WordbooksResultSetDto((long) results.size(), 1L, results);
        return ResponseEntity.ok(resultSet);
    }

    /**
     * 단일 wordbook 가져오기
     *
     * @param id {Long}
     * @return
     */
    @GetMapping("/api/wordbooks/{id}")
    public ResponseEntity<WordbookDetailDto> GetWordbookUpdate(@PathVariable(value = "id") Long id) {
        WordbookDetailDto dto = wordbookService.findOne(id);
        return ResponseEntity.ok(dto);
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
    public ResponseEntity<?> PostWordbookUpdate(@PathVariable(value = "id") Long id, @RequestBody WordbookUpdateDto wordbook) throws JsonProcessingException {
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
    public ResponseEntity<?> DeleteWordbook(@PathVariable(value = "id") Long id) {
        wordbookService.removeById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 조회 문제
     * - wordbook 이 없습니다
     *
     * @param e EntityNotFoundException
     * @return ErrorType.WordbookNotFound
     */
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorType entityNotFoundException(Exception e) {
        return ErrorType.WordbookNotFound;
    }


}
