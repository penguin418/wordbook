package penguin.wordbook.controller;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class WordbookControllerAdvice {
    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.initBeanPropertyAccess(); // 필드로 접근
    }
}
