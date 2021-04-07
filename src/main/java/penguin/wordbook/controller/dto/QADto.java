package penguin.wordbook.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

public class QADto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor @Builder
    public static class QACreateDto{

        private String question;

        private String answer;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor @Builder
    public static class QAUpdateDto{

        private Long qaId;

        private String question;

        private String answer;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor @Builder
    public static class QAResponseDto{

        private Long qaId;

        private String question;

        private String answer;
    }
}
