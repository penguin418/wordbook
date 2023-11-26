package penguin.wordbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class WordbookController {
    /**
     * 홈
     * @return 홈 페이지
     */
    @GetMapping("/")
    public String home() {
        return "index";
    }

    /**
     * wordbook 생성
     * @return wordbook 생성 페이지
     */
    @GetMapping("/wordbooks/create")
    public String newWordbook() {
        return "wordbooks/create";
    }

    /**
     * wordbook 전체 조회
     * @return wordbook 전체 조회 페이지
     */
    @GetMapping("/wordbooks/list")
    public String wordbookList() {
        return "wordbooks/list";
    }

    /**
     * wordbook 조회
     * @return wordbook 조회 페이지
     */
    @GetMapping("/wordbooks/{id}")
    public String wordbookItem(@PathVariable(value = "id") Long id) {
        return "wordbooks/item";
    }

    /**
     * wordbook 갱신
     * @return
     */
    @GetMapping("/wordbooks/{id}/update")
    public String wordbookItemDetail(@PathVariable(value = "id") Long id) {
        return "wordbooks/update";
    }
}
