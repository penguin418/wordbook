package penguin.wordbook.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import penguin.wordbook.domain.Wordbook;
import penguin.wordbook.service.WordbookService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Long.parseLong;

@Controller
public class WordbookController {
    private final WordbookService wordbookService;

    @Autowired
    public WordbookController(WordbookService wordbookService) {
        this.wordbookService = wordbookService;
    }

    @GetMapping("/")
    public String Home(Model model){return "home";}

    @GetMapping("/wordbook/create")
    public String WordbookCreate(Model model){return "wordbook/create";}

    @GetMapping("/wordbook/list")
    public String WordbookList(Model model){
        List<Wordbook> wordbookList = wordbookService.findAll();
        model.addAttribute("wordbooks", wordbookList);
        return "wordbook/list";}

    @GetMapping("/wordbook/{id}")
    public String Wordbook(@PathVariable(value="id") String id, Model model) {
        Wordbook wordbook;
        try{
            Long wordbookId = Long.parseLong(id);
            wordbook = wordbookService.findOne(wordbookId).get();
            model.addAttribute("wordbook", wordbook);
            model.addAttribute("qaList", wordbook.getQaList());
            return "wordbook/item";
        }catch (NumberFormatException e){
            return "error/404";
        }
    }
}
