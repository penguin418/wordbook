package penguin.wordbook.mapper;

import static penguin.wordbook.controller.dto.WordbookDto.*;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import penguin.wordbook.domain.Wordbook;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, uses = {QAMapper.class})
public interface WordbookMapper {

    @Mapping(target = "id", ignore = true)
    public Wordbook mapToDetailDto(WordbookCreateDto dto);

    public Wordbook mapToDetailDto(WordbookUpdateDto dto);

    public WordbookDetailDto mapToDetailDto(Wordbook entity);

    public WordbookItemDto mapToItemDto(Wordbook entity);
}
