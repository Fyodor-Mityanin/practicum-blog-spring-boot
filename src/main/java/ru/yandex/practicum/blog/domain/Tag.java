package ru.yandex.practicum.blog.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    private Long id;
    private String name;
    private Long postId;

    public Tag(long id, String name) {
        this.id = id;
        this.name = name;
    }
}