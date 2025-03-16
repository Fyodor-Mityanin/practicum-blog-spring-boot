package ru.yandex.practicum.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.blog.domain.Like;
import ru.yandex.practicum.blog.domain.Post;
import ru.yandex.practicum.blog.repository.LikeRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;

    public void likePost(Post post) {
        Like like = new Like();
        like.setPostId(post.getId());
        likeRepository.save(like);
    }

    public Map<Long, List<Like>> getByPostIds(List<Long> postIds) {
        List<Like> likes = likeRepository.getByPostIds(postIds);
        return likes.stream().collect(Collectors.groupingBy(Like::getPostId));
    }
}
