package penguin.wordbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {
    /**
     * account 생성
     *
     * @return account 생성 페이지
     */
    @GetMapping("/accounts/create")
    public String create() {
        return "accounts/create";
    }

    /**
     * account 로그인
     *
     * @return account 로그인 페이지
     */
    @GetMapping("/accounts/login")
    public String login() {
        return "accounts/login";
    }

    /**
     * account 계정 정보
     *
     * @return account 갱신 페이지
     */
    @GetMapping("/accounts/detail")
    public String details() {
        return "accounts/detail";
    }
}