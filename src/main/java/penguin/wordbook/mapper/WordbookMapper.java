package penguin.wordbook.mapper;

import static penguin.wordbook.controller.dto.WordbookDto.*;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import penguin.wordbook.domain.Wordbook;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, uses = {QAMapper.class, AccountMapper.class})
public interface WordbookMapper {

    @Mapping(target = "wordbookId", ignore = true)
    public Wordbook mapToDetailDto(WordbookCreateDto dto);

    public Wordbook mapToDetailDto(WordbookUpdateDto dto);

    public WordbookDetailDto mapToDetailDto(Wordbook entity);

    @Mapping(source = "wordbookId", target = "wordbookId")
    public WordbookItemDto mapToItemDto(Wordbook entity);
}
