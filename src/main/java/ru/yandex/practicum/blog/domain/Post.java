package ru.yandex.practicum.blog.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class Post {
    public Post(long id, String title, String content, byte[] images, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.image = images;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    private Long id;
    private String title;
    private String content;
    private byte[] image;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<Comment> comments = new LinkedHashSet<>();
    private Set<Like> likes = new LinkedHashSet<>();
    private Set<Tag> tags = new LinkedHashSet<>();
}
