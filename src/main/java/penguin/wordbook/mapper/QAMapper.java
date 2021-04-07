package penguin.wordbook.mapper;

import static penguin.wordbook.controller.dto.QADto.*;

import org.hibernate.criterion.DetachedCriteria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import penguin.wordbook.domain.QA;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface QAMapper {

    @Mapping(target = "qaId", ignore = true)
    @Mapping(target = "wordbook", ignore = true)
    public QA fromCreateDto(QACreateDto dto);

    @Mapping(target = "wordbook", ignore = true)
    public QA fromUpdateDto(QAUpdateDto dto);

    public Set<QA> fromCreateDto(Set<QACreateDto> dtoList);

    public Set<QA> fromUpdateDto(Set<QAUpdateDto> dtoList);

    public QAResponseDto toResponseDto(QA entity);

    public QAUpdateDto toUpdateDto(QA entity);
}
