package ru.yandex.practicum.blog.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PostRequestDto {
    public String title;
    public String content;
    public String tags;
    public MultipartFile image;
}
