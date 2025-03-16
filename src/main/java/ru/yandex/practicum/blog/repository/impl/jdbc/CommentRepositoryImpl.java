package ru.yandex.practicum.blog.repository.impl.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.blog.domain.Comment;
import ru.yandex.practicum.blog.repository.CommentRepository;

import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {
    public static final String SAVE_QUERY = "INSERT INTO comments (post_id, content) VALUES (?, ?)";
    public static final String DELETE_QUERY = "DELETE FROM comments WHERE id = ?";
    public static final String FIND_BY_POST_QUERY = """
            SELECT c.*
            FROM comments c
            WHERE c.post_id IN (%s)
            """;


    private final JdbcTemplate jdbcTemplate;


    @Override
    public void save(Comment comment) {
        jdbcTemplate.update(
                SAVE_QUERY,
                comment.getPostId(),
                comment.getContent()
        );
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }

    @Override
    public List<Comment> getByPostIds(List<Long> postIds) {
        String inSql = String.join(",", Collections.nCopies(postIds.size(), "?"));
        return jdbcTemplate.query(
                String.format(FIND_BY_POST_QUERY, inSql),
                (rs, rowNum) -> new Comment(
                        rs.getLong("id"),
                        rs.getLong("post_id"),
                        rs.getString("content"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime()
                ),
                postIds.toArray()
        );
    }
}
