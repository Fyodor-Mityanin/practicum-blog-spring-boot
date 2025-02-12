package ru.yandex.practicum.blog.repository.impl.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.blog.domain.Like;
import ru.yandex.practicum.blog.repository.LikeRepository;

import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepository {
    public static final String SAVE_QUERY = "INSERT INTO likes (post_id, user_id) VALUES (?, ?)";
    public static final String FIND_BY_POST_QUERY = """
            SELECT l.*
            FROM likes l
            WHERE l.post_id IN (%s)
            """;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void save(Like like) {
        jdbcTemplate.update(
                SAVE_QUERY,
                like.getPostId(),
                1
        );
    }

    @Override
    public List<Like> getByPostIds(List<Long> postIds) {
        String inSql = String.join(",", Collections.nCopies(postIds.size(), "?"));
        return jdbcTemplate.query(
                String.format(FIND_BY_POST_QUERY, inSql),
                (rs, rowNum) -> new Like(
                        rs.getLong("id"),
                        rs.getLong("post_id"),
                        rs.getLong("user_id"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                ),
                postIds.toArray()
        );
    }
}
