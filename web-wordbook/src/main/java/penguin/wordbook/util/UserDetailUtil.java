package penguin.wordbook.util;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import penguin.wordbook.config.UserDetail;
import penguin.wordbook.controller.dto.AccountDto;

@Component
@RequiredArgsConstructor
public class UserDetailUtil {
    public static void validateUserInfo(UserDetail userDetail, AccountDto.AccountInfoDto userInfo) {
        if (!userDetail.getAccountId().equals(userInfo.getAccountId()))
            throw new ValidationFailureException("id");
        if (!userDetail.getNickname().equals(userInfo.getNickname()))
            throw new ValidationFailureException("nickname");
        if (!userDetail.getUsername().equals(userInfo.getEmail()))
            throw new ValidationFailureException("email");
    }

    private static class ValidationFailureException extends RuntimeException {
        public ValidationFailureException(String id) {
        }
    }
}
