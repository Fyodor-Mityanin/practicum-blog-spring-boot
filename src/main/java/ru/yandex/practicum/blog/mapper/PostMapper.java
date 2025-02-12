package ru.yandex.practicum.blog.mapper;

import lombok.SneakyThrows;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.blog.domain.Post;
import ru.yandex.practicum.blog.dto.request.PostRequestDto;
import ru.yandex.practicum.blog.dto.response.PostDto;

import java.util.Base64;

@Mapper(uses = TagMapper.class)
public interface PostMapper {

    PostDto toModel(Post source);

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "likes", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "tags", ignore = true)
    Post toEntity(PostRequestDto post);

    @SneakyThrows
    default byte[] toBytes(MultipartFile source) {
        if (source == null) {
            return null;
        }
        return source.getBytes();
    }

    default String toBase64(byte[] source) {
        if (source == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(source);
    }
}
