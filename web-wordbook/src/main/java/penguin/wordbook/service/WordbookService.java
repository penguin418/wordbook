package penguin.wordbook.service;

import static penguin.wordbook.controller.dto.WordbookDto.*;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import penguin.wordbook.config.DatasourceWordbookConfiguration;
import penguin.wordbook.model.entity.QA;
import penguin.wordbook.model.entity.Wordbook;
import penguin.wordbook.mapper.QAMapper;
import penguin.wordbook.mapper.WordbookMapper;
import penguin.wordbook.dao.repository.QARepository;
import penguin.wordbook.dao.repository.WordbookRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(transactionManager = DatasourceWordbookConfiguration.TRANSACTION_MANAGER)
public class WordbookService {

    private final WordbookRepository wordbookRepository;
    private final QARepository qaRepository;
    private final WordbookMapper wordbookMapper;
    private final QAMapper qaMapper;

    /**
     * 생성
     *
     * @param dto {WordbookCreateDto} 생성 요청
     * @return WordbookDetailDto 생성된 wordbook
     */
    public WordbookDetailDto create(WordbookCreateDto dto) {
        Wordbook wordbook = wordbookMapper.mapToDetailDto(dto);
        Set<QA> qaList = qaMapper.fromCreateDto(dto.getQaList());
        wordbook.setQaList(qaList);
        wordbookRepository.save(wordbook);
        return wordbookMapper.mapToDetailDto(wordbook);
    }

    /**
     * 조회
     *
     * @param id {Long} wordbook 아이디
     * @return WordbookDetailDto qa가 담긴 자세한 wordbook
     */
    public WordbookDetailDto findOne(Long id) {
        Wordbook wordbook = wordbookRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return wordbookMapper.mapToDetailDto(wordbook);
    }

    /**
     * 전체 조회
     *
     * @return List 간단한 wordbook 내역
     * TODO: pagination 구현 후 return 타입 WordbooksResultSetDto로 교체
     */
    public List<WordbookItemDto> findAll() {
        return wordbookRepository.findAll().stream().map(wordbookMapper::mapToItemDto).collect(Collectors.toList());
    }

    public List<WordbookItemDto> findByAccountId(Long accountId) {
        return wordbookRepository.findByAccountId(accountId).stream().map(wordbookMapper::mapToItemDto).collect(Collectors.toList());
    }

    /**
     * 갱신
     *
     * @param dto {WordbookUpdateDto}
     */
    @Transactional
    public WordbookDetailDto update(WordbookUpdateDto dto) {
        Wordbook wordbookFromRepo = wordbookRepository.findById(dto.getWordbookId()).orElseThrow(EntityNotFoundException::new);
        wordbookFromRepo.setName(dto.getName());
        wordbookFromRepo.setDescription(dto.getDescription());
        Set<QA> qaListToAdd = qaMapper.fromUpdateDto(dto.getQaList());
        wordbookFromRepo.setQaList(qaListToAdd);
        wordbookRepository.save(wordbookFromRepo);
        return wordbookMapper.mapToDetailDto(wordbookFromRepo);
    }

    /**
     * 삭제
     *
     * @param id {Long}
     */
    public void removeById(Long id) {
        wordbookRepository.deleteById(id);
    }
}
