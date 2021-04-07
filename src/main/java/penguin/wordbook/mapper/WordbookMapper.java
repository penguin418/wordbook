package penguin.wordbook.mapper;

import static penguin.wordbook.controller.dto.WordbookDto.*;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import penguin.wordbook.controller.dto.QADto;
import penguin.wordbook.domain.QA;
import penguin.wordbook.domain.Wordbook;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        uses = {QAMapper.class, AccountMapper.class})
public abstract class WordbookMapper {
    @Autowired protected QAMapper qaMapper;

    @Mapping(target = "wordbookId", ignore = true)
    public abstract Wordbook mapToDetailDto(WordbookCreateDto dto);

    @Mapping(target = "qaList", source = "qaList")
    public abstract WordbookDetailDto mapToDetailDto(Wordbook entity);

    @Mapping(source = "wordbookId", target = "wordbookId")
    public abstract WordbookItemDto mapToItemDto(Wordbook entity);
}
