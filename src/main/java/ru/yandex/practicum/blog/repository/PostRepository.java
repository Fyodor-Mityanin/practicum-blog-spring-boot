package ru.yandex.practicum.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.blog.domain.Post;

@Repository
public interface PostRepository {
    Page<Post> findByTagsNameIgnoreCase(String name, Pageable pageable);
    Page<Post> findAll(Pageable pageable);
    void saveWithTags(Post entity);
    Post getReferenceById(Long id);
    void deleteById(Long id);
    void update(Post entity);
}