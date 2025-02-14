package ru.yandex.practicum.blog.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class PostControllerIntegrationTest {

    @Autowired
    private PostController postController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(postController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    @Sql({
            "sql/posts.sql",
            "sql/tags.sql",
            "sql/post_tags.sql",
            "sql/comments.sql",
            "sql/likes.sql",
    })
    void getPosts_shouldReturnHtmlWithPosts() throws Exception {
        mockMvc.perform(get("/blog"))
                .andExpect(status().isOk());
    }

    @Test
    @Sql({
            "sql/posts.sql",
            "sql/tags.sql"
    })
    void savePost_shouldSavePost() throws Exception {
        mockMvc.perform(post("/blog/post")
                        .param("title", "title")
                        .param("content", "content")
                        .param("tags", "tag1,tag2,tag3")
                        .content(new byte[]{1, 2, 3})
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                )
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @Sql({"sql/posts.sql",
            "sql/tags.sql",
            "sql/post_tags.sql",
            "sql/comments.sql",
            "sql/likes.sql"})
    void getPost_shouldReturnHtmlWithPost() throws Exception {
        mockMvc.perform(get("/blog/post/2"))
                .andExpect(status().isOk());
    }

    @Test
    @Sql("sql/posts.sql")
    void deletePost_shouldDeletePost() throws Exception {
        mockMvc.perform(delete("/blog/post/2"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @Sql({"sql/posts.sql", "sql/tags.sql"})
    void updatePost_shouldUpdatePost() throws Exception {
        mockMvc.perform(put("/blog/post/2/edit")
                        .param("title", "newTitle")
                        .param("content", "newcontent")
                        .param("tags", "tag11,tag33")
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                )
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @Sql("sql/posts.sql")
    void likePost_shouldLikePost() throws Exception {
        mockMvc.perform(get("/blog/post/2/like"))
                .andExpect(status().isOk());
    }
}
