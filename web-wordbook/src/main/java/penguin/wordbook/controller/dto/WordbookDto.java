package penguin.wordbook.controller.dto;

import static penguin.wordbook.controller.dto.QADto.*;
import static penguin.wordbook.controller.dto.AccountDto.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordbookDto {

    /**
     * wordbook 생성 시 사용되는 dto
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WordbookCreateDto {

        private AccountInfoDto account;

        private String name;

        private String description;

        @Builder.Default
        private Set<QACreateDto> qaList = new HashSet<>();

        public WordbookCreateDto(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }

    /**
     * wordbook 갱신 시 사용되는 dto
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WordbookUpdateDto {

        private Long wordbookId;

        private AccountInfoDto account;

        private String name;

        private String description;

        @Builder.Default
        private Set<QAUpdateDto> qaList = new HashSet<>();
    }

    /**
     * wordbook 단일 조회 결과로 사용되는 dto
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WordbookDetailDto {

        private Long wordbookId;

        private AccountInfoDto account;

        private String name;

        private String description;

        @Builder.Default
        private Set<QAResponseDto> qaList = new HashSet<>();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WordbookItemDto {

        private Long wordbookId;

        private AccountInfoDto account;

        private String name;

        private String description;
    }


    /**
     * wordbook 전체 조회 결과로 사용되는 dto
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WordbooksResultSetDto {

        private Long total;

        private Long page;

        List<WordbookItemDto> contents;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MyWordbookResultSetDto {
        private Long total;

        private Long page;

        List<WordbookItemDto> contents;
    }
}
