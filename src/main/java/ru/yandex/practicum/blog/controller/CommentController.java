package ru.yandex.practicum.blog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.blog.dto.request.CommentRequestDto;
import ru.yandex.practicum.blog.service.PostService;

@Controller
@RequestMapping("/blog/post")
@RequiredArgsConstructor
public class CommentController {
    private final PostService postService;

    @PostMapping(path = "/{id}/comment")
    public String addComment(@PathVariable("id") Long id, @ModelAttribute CommentRequestDto comment) {
        postService.addComment(id, comment);
        return "redirect:/blog/post/" + id;
    }

    @DeleteMapping(path = "/{id}/comment/{commentId}")
    public void deleteComment(@PathVariable("id") Long id, @PathVariable("commentId") Long commentId) {
        postService.deleteComment(id, commentId);
    }
}
