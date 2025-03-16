package ru.yandex.practicum.blog.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class LikeDto {
    private Long userId;
    private LocalDateTime createdAt;
}
