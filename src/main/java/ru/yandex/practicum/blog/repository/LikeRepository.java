package ru.yandex.practicum.blog.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.blog.domain.Like;

import java.util.List;

@Repository
public interface LikeRepository {
    void save(Like like);
    List<Like> getByPostIds(List<Long> postIds);
}
