package penguin.wordbook.mapper;

import static penguin.wordbook.controller.dto.QADto.*;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import penguin.wordbook.domain.QA;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface QAMapper {

    @Mapping(target = "id", ignore = true)
    public QA map(QACreateDto dto);

    public QA map(QAUpdateDto dto);

    public QAResponseDto map(QA entity);
}
