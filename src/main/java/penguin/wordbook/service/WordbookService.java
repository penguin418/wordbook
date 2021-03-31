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

    public WordbookDetailDto create(WordbookCreateDto dto) {
        Wordbook wordbook = wordbookMapper.mapToDetailDto(dto);
        wordbookRepository.save(wordbook);
        return wordbookMapper.mapToDetailDto(wordbook);
    }

    public WordbookDetailDto findOne(Long id) {
        Wordbook wordbook = wordbookRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return wordbookMapper.mapToDetailDto(wordbook);
    }

    public List<WordbookItemDto> findAll() {
        return wordbookRepository.findAll().stream().map(wordbookMapper::mapToItemDto).collect(Collectors.toList());
    }

    public void update(WordbookUpdateDto dto) {
        Wordbook wordbook = wordbookMapper.mapToDetailDto(dto);
        wordbookRepository.save(wordbook);
    }

    public void removeById(Long id) {
        wordbookRepository.deleteById(id);
    }
}
