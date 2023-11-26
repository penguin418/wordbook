package penguin.wordbook.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AccountDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AccountCreateDto {
        private String nickname;
        private String email;
        private String password;

        public void setPassword(String password) {
            this.password = password;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AccountLoginDto {
        private String email;
        private String password;

        public void setPassword(String password) {
            this.password = password;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AccountInfoDto {
        private Long accountId;
        private String nickname;
        private String email;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AccountUpdateDto {
        private Long accountId;
        private String nickname;
        private String email;
        private String password;
        private String newPassword;

        /**
         * service 레이어에서 비밀번호를 해시하여 저장할 때 사용됨
         * @param hashed_password {String} 해시된 비밀번호
         */
        public void setPassword(String hashed_password) {
            this.password = hashed_password;
        }
    }


}
