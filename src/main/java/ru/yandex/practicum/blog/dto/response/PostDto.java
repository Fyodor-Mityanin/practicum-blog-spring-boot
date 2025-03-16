package ru.yandex.practicum.blog.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Accessors(chain = true)
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<CommentDto> comments = new LinkedHashSet<>();
    private Set<LikeDto> likes = new LinkedHashSet<>();
    private Set<TagDto> tags = new LinkedHashSet<>();
}
