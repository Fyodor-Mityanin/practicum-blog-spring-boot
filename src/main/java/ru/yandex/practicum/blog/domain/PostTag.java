package ru.yandex.practicum.blog.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostTag {
    private Long postId;
    private Long tagId;
}
