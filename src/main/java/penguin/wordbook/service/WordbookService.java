package penguin.wordbook.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import penguin.wordbook.domain.Wordbook;
import penguin.wordbook.repository.WordbookRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WordbookService {
    private final WordbookRepository wordbookRepository;


    public WordbookService(WordbookRepository wordbookRepository) {
        this.wordbookRepository = wordbookRepository;
    }

    public Wordbook create(Wordbook wordbook){
        return wordbookRepository.save(wordbook);
    }

    public Optional<Wordbook> findOne(Long id){ return wordbookRepository.findById(id);}

    public List<Wordbook> findAll(){
        return wordbookRepository.findAll();
    }

    public boolean remove(Wordbook wordbook){
        return wordbookRepository.delete(wordbook);
    }

}
