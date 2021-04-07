package penguin.wordbook.service;

import static penguin.wordbook.controller.dto.WordbookDto.*;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import penguin.wordbook.domain.Wordbook;
import penguin.wordbook.mapper.WordbookMapper;
import penguin.wordbook.repository.QARepository;
import penguin.wordbook.repository.WordbookRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class WordbookService {

    private final WordbookRepository wordbookRepository;
    private final QARepository qaRepository;
    private final WordbookMapper wordbookMapper;

    /**
     * 생성
     * @param dto {WordbookCreateDto} 생성 요청
     * @return WordbookDetailDto 생성된 wordbook
     */
    public WordbookDetailDto create(WordbookCreateDto dto) {
        Wordbook wordbook = wordbookMapper.mapToDetailDto(dto);
        wordbookRepository.save(wordbook);
        return wordbookMapper.mapToDetailDto(wordbook);
    }

    /**
     * 조회
     * @param id {Long} wordbook 아이디
     * @return WordbookDetailDto qa가 담긴 자세한 wordbook
     */
    public WordbookDetailDto findOne(Long id) {
        Wordbook wordbook = wordbookRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return wordbookMapper.mapToDetailDto(wordbook);
    }

    /**
     * 전체 조회
     * @return List 간단한 wordbook 내역
     * TODO: pagination 구현 후 return 타입 WordbooksResultSetDto로 교체
     */
    public List<WordbookItemDto> findAll() {
        return wordbookRepository.findAll().stream().map(wordbookMapper::mapToItemDto).collect(Collectors.toList());
    }

    public List<WordbookItemDto> findByAccountId(Long accountId){
        return wordbookRepository.findByAccountAccountId(accountId).stream().map(wordbookMapper::mapToItemDto).collect(Collectors.toList());
    }

    /**
     * 갱신
     * @param dto {WordbookUpdateDto}
     */
    public void update(WordbookUpdateDto dto) {
        Wordbook wordbook = wordbookMapper.mapToDetailDto(dto);
        wordbookRepository.save(wordbook);
    }

    /**
     * 삭제
     * @param id {Long}
     */
    public void removeById(Long id) {
        wordbookRepository.deleteById(id);
    }
}
