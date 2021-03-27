package penguin.wordbook.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import penguin.wordbook.domain.Wordbook;
import penguin.wordbook.repository.QARepository;
import penguin.wordbook.repository.WordbookRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class WordbookService {

    private final QARepository qaRepository;
    private final WordbookRepository wordbookRepository;

    public Wordbook create(Wordbook wordbook){
        return wordbookRepository.save(wordbook);
    }

    public Optional<Wordbook> findOne(Long id){ return wordbookRepository.findById(id);}

    public List<Wordbook> findAll(){
        return wordbookRepository.findAll();
    }

    public void update(Wordbook wordbook) {
        wordbookRepository.save(wordbook);
    }

    public void remove(Wordbook wordbook){
        wordbookRepository.delete(wordbook);
    }

    public void removeById(Long id){
        wordbookRepository.deleteById(id);
    }
}
