package ru.yandex.practicum.blog.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class CommentControllerIntegrationTest {

    @Autowired
    private CommentController commentController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }


    @Test
    @Sql("sql/posts.sql")
    void addComment_shouldAddComment() throws Exception {
        mockMvc.perform(post("/blog/post/2/comment")
                        .param("text", "texteext")
                )
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @Sql({"sql/posts.sql", "sql/comments.sql"})
    void deleteComment_shouldDeleteComment() throws Exception {
        mockMvc.perform(delete("/blog/post/2/comment/2"))
                .andExpect(status().isOk());
    }
}
