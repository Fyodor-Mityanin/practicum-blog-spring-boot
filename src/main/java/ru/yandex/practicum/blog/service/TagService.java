package ru.yandex.practicum.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.blog.domain.Tag;
import ru.yandex.practicum.blog.dto.response.TagDto;
import ru.yandex.practicum.blog.mapper.TagMapper;
import ru.yandex.practicum.blog.repository.TagRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public List<String> getAllTags() {
        return  tagMapper.toModels(tagRepository.findAll()).stream()
                .map(TagDto::getName)
                .collect(Collectors.toList());
    }

    public Set<Tag> save(String tags) {
        Set<String> tagsSplit = Arrays.stream(tags.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        return new HashSet<>(tagRepository.getOrCreate(tagsSplit));
    }

    public Map<Long, List<Tag>> getByPostIds(List<Long> postIds) {
        List<Tag> tags = tagRepository.getTagsByPostIds(postIds);
        return tags.stream().collect(Collectors.groupingBy(Tag::getPostId));
    }
}
