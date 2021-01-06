package penguin.wordbook.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import penguin.wordbook.domain.QA;
import penguin.wordbook.domain.Wordbook;
import penguin.wordbook.service.WordbookService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.NoSuchElementException;
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
    public String GetWordbookCreate(Model model){
        return "wordbook/create";
    }

    @PostMapping("/wordbook/create")
    public String PostWordbookCreate(Wordbook wordbook) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(wordbook));

        Wordbook newBook = wordbookService.create(wordbook);
        return "redirect:/wordbook/" + newBook.getId();
    }

    @GetMapping("/wordbook/{id}/update")
    public String GetWordbookUpdate(@PathVariable(value="id") String id,Model model){
        try{
            Long wordbookId = Long.parseLong(id);
            Wordbook wordbook = wordbookService.findOne(wordbookId)
                    .orElseThrow(NoSuchElementException::new);
            model.addAttribute("wordbook", wordbook);
            model.addAttribute("qaList", wordbook.getQaList());
            return "wordbook/update";
        }catch (NumberFormatException | NoSuchElementException e){
            return "error/404";
        }
    }

    @PostMapping("/wordbook/update")
    public String PostWordbookUpdate(Wordbook wordbook) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(wordbook));


        wordbookService.update(wordbook);
        return "redirect:/wordbook/list";
    }

    @GetMapping("/wordbook/list")
    public String GetWordbookList(Model model){
        List<Wordbook> wordbookList = wordbookService.findAll();
        model.addAttribute("wordbooks", wordbookList);
        return "wordbook/list";
    }

    @GetMapping("/wordbook/{id}")
    public String GetWordbook(@PathVariable(value="id") String id, Model model) {
        try{
            Long wordbookId = Long.parseLong(id);
            Wordbook wordbook = wordbookService.findOne(wordbookId)
                    .orElseThrow(NoSuchElementException::new);
            model.addAttribute("wordbook", wordbook);
            model.addAttribute("qaList", wordbook.getQaList());
            return "wordbook/item";
        }catch (NumberFormatException | NoSuchElementException e){
            return "error/404";
        }
    }
    @DeleteMapping("/wordbook/{id}")
    @ResponseBody
    public String DeleteWordbook(@PathVariable(value="id") String id, Model model) {
        try{
            Long wordbookId = Long.parseLong(id);
            wordbookService.removeById(wordbookId);
            return "success";
        }catch (NumberFormatException e){
            return "500";
        }catch (Exception e){
            System.out.println(e);
            return "404";
        }
    }
}
