package penguin.wordbook.controller.dto;

import static penguin.wordbook.controller.dto.QADto.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

public class WordbookDto {

    /**
     * wordbook 생성 시 사용되는 dto
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WordbookCreateDto {

        private String name;

        private String description;

        @Builder.Default
        private List<QACreateDto> qaList = new ArrayList<>();

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

        private Long id;

        private String name;

        private String description;

        @Builder.Default
        private List<QAUpdateDto> qaList = new ArrayList<>();
    }

    /**
     * wordbook 단일 조회 결과로 사용되는 dto
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WordbookDetailDto {

        private Long id;

        private String name;

        private String description;

        @Builder.Default
        private List<QAResponseDto> qaList = new ArrayList<>();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WordbookItemDto {

        private Long id;

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
}
