package ru.yandex.practicum.blog.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.blog.domain.Tag;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface TagRepository {
    List<Tag> findByNameInIgnoreCase(Collection<String> names);
    List<Tag> save(List<Tag> tags);
    List<Tag> getOrCreate(Set<String> tagNames);
    List<Tag> findAll();
    List<Tag> getTagsByPostIds(List<Long> postIds);
}
