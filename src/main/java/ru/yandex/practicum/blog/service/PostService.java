package ru.yandex.practicum.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.blog.domain.Comment;
import ru.yandex.practicum.blog.domain.Like;
import ru.yandex.practicum.blog.domain.Post;
import ru.yandex.practicum.blog.domain.Tag;
import ru.yandex.practicum.blog.dto.request.CommentRequestDto;
import ru.yandex.practicum.blog.dto.request.PostRequestDto;
import ru.yandex.practicum.blog.dto.response.PostDto;
import ru.yandex.practicum.blog.mapper.PostMapper;
import ru.yandex.practicum.blog.repository.PostRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final TagService tagService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final PostMapper postMapper;

    public Page<PostDto> findByTag(String tag, Pageable pageable) {
        Page<Post> posts;
        if (tag == null || tag.isEmpty()) {
            posts = postRepository.findAll(pageable);
        } else {
            posts = postRepository.findByTagsNameIgnoreCase(tag, pageable);
        }
        List<Long> postIds = posts.stream().map(Post::getId).toList();
        Map<Long, List<Tag>> tags = tagService.getByPostIds(postIds);
        Map<Long, List<Comment>> comments = commentService.getByPostIds(postIds);
        Map<Long, List<Like>> likes = likeService.getByPostIds(postIds);
        posts.forEach(post -> {
            post.getTags().addAll(tags.getOrDefault(post.getId(), Collections.emptyList()));
            post.getComments().addAll(comments.getOrDefault(post.getId(), Collections.emptyList()));
            post.getLikes().addAll(likes.getOrDefault(post.getId(), Collections.emptyList()));
        });
        return posts.map(postMapper::toModel);
    }

    public void save(PostRequestDto dto) {
        Post entity = postMapper.toEntity(dto);
        entity.setTags(tagService.save(dto.getTags()));
        postRepository.saveWithTags(entity);
    }

    public PostDto getById(Long id) {
        return postMapper.toModel(postRepository.getReferenceById(id));
    }

    public void removeById(Long id) {
        postRepository.deleteById(id);
    }

    public void update(Long id, PostRequestDto dto) {
        Post entity = postRepository.getReferenceById(id);
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setTags(tagService.save(dto.getTags()));
        postRepository.update(entity);
    }

    public void addComment(Long id, CommentRequestDto dto) {
        Post post = postRepository.getReferenceById(id);
        commentService.save(post, dto);
    }

    public void deleteComment(Long id, Long commentId) {
        postRepository.getReferenceById(id).getComments().stream()
                .filter(it -> it.getId().equals(commentId))
                .findFirst()
                .ifPresent(commentService::remove);
    }

    public void like(Long id) {
        likeService.likePost(postRepository.getReferenceById(id));
    }
}