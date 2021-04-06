package penguin.wordbook.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 에러코드는 1000 단위도 서비스별로 구분할 것
 */
@Getter
@AllArgsConstructor
public enum ErrorType {
    NicknameExists(1001, "이미 사용중인 닉네임입니다"),
    EmailExists(1002, "이미 사용중인 이메일입니다"),
    AccountNotFound(1003, "존재하지 않는 account_id 입니다"),
    PasswordDoesNotMatch(1004,"비밀번호가 일치하지 않습니다"),
    UsernameNotFound(1005, "유저를 찾을 수 없습니다");
    private final Integer code;
    private final String message;
}
