package ru.yandex.practicum.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.blog.domain.Comment;
import ru.yandex.practicum.blog.domain.Post;
import ru.yandex.practicum.blog.dto.request.CommentRequestDto;
import ru.yandex.practicum.blog.mapper.CommentMapper;
import ru.yandex.practicum.blog.repository.CommentRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    public void save(Post post, CommentRequestDto dto) {
        commentRepository.save(commentMapper.toEntity(dto, post));
    }

    public void remove(Comment entity) {
        commentRepository.delete(entity.getId());
    }

    public Map<Long, List<Comment>> getByPostIds(List<Long> postIds) {
        List<Comment> comments = commentRepository.getByPostIds(postIds);
        return comments.stream().collect(Collectors.groupingBy(Comment::getPostId));
    }
}
