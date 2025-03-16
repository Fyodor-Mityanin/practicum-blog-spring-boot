package ru.yandex.practicum.blog.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.blog.domain.Tag;
import ru.yandex.practicum.blog.dto.response.TagDto;

import java.util.List;

@Mapper
public interface TagMapper {
    TagDto toModel(Tag source);

    List<TagDto> toModels(List<Tag> source);
}
