package penguin.wordbook.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 에러코드는 1000 단위도 서비스별로 구분할 것
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
@AllArgsConstructor
public enum ErrorType {
    NicknameExists(1001, "11111"),
    EmailExists(1002, "이미 사용중인 이메일입니다"),
    AccountNotFound(1003, "존재하지 않는 account 입니다"),
    PasswordDoesNotMatch(1004,"3333"),
    UsernameNotFound(1005, "유저를 찾을 수 없습니다"),

    WordbookNotFound(2001, "존재하지 않는 wordbook 입니다");
    private final Integer code;
    private final String message;
}
