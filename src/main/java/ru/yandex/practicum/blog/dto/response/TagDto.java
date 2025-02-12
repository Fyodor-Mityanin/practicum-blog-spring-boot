package ru.yandex.practicum.blog.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TagDto {
    private String name;
}
