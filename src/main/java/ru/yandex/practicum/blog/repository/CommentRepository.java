package ru.yandex.practicum.blog.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.blog.domain.Comment;

import java.util.List;

@Repository
public interface CommentRepository {
    void save(Comment entity);
    void delete(Long id);
    List<Comment> getByPostIds(List<Long> postIds);
}
