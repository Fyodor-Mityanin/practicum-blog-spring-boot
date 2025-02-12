package ru.yandex.practicum.blog.repository.impl.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.blog.domain.Post;
import ru.yandex.practicum.blog.domain.Tag;
import ru.yandex.practicum.blog.repository.PostRepository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {
    public static final String COUNT_QUERY_BY_TAG_NAME = """
            SELECT COUNT(DISTINCT p.id) FROM posts p
            JOIN post_tags pt ON p.id = pt.post_id
            JOIN tags t ON pt.tag_id = t.id
            WHERE LOWER(t.name) = LOWER(?)
            """;
    public static final String COUNT_QUERY_ALL = "SELECT COUNT(DISTINCT p.id) FROM posts p";
    public static final String FIND_BY_TAG_NAME_QUERY = """
            SELECT DISTINCT p.*
            FROM posts p
            LEFT JOIN post_tags pt ON p.id = pt.post_id
            LEFT JOIN tags t ON pt.tag_id = t.id
            WHERE LOWER(t.name) = LOWER(?)
            LIMIT 3 OFFSET 1
            """;

    public static final String FIND_ALL_QUERY = """
            SELECT DISTINCT p.*
            FROM posts p
            LEFT JOIN post_tags pt ON p.id = pt.post_id
            LEFT JOIN tags t ON pt.tag_id = t.id
            LIMIT ? OFFSET ?
            """;
    public static final String SAVE_QUERY = "INSERT INTO posts (title, content, image) VALUES (?, ?, ?)";
    public static final String UPDATE_QUERY = "UPDATE posts SET title = ?, content = ?, image = ? WHERE id = ?";
    public static final String POST_TAG_SAVE_QUERY = "INSERT INTO post_tags (post_id, tag_id) VALUES (?, ?)";
    public static final String FIND_BY_ID_QUERY = """
            SELECT p.*
            FROM posts p
            WHERE p.id = ?
            """;
    public static final String DELETE_QUERY = "DELETE FROM posts WHERE id = ?";


    private final JdbcTemplate jdbcTemplate;

    @Override
    public Page<Post> findByTagsNameIgnoreCase(String name, Pageable pageable) {
        int limit = pageable.getPageSize();
        int offset = pageable.getPageNumber() * limit;
        List<Post> posts = jdbcTemplate.query(
                FIND_BY_TAG_NAME_QUERY,
                (rs, rowNum) ->
                        new Post(
                                rs.getLong("id"),
                                rs.getString("title"),
                                rs.getString("content"),
                                rs.getBytes("image"),
                                rs.getTimestamp("created_at").toLocalDateTime(),
                                rs.getTimestamp("updated_at").toLocalDateTime()
                        ),
                name,
                limit,
                offset
        );

        int total = jdbcTemplate.queryForObject(COUNT_QUERY_BY_TAG_NAME, Integer.class, name);
        return new PageImpl<>(posts, pageable, total);
    }

    @Override
    public Page<Post> findAll(Pageable pageable) {
        int limit = pageable.getPageSize();
        int offset = pageable.getPageNumber() * limit;
        List<Post> posts = jdbcTemplate.query(
                FIND_ALL_QUERY,
                (rs, rowNum) ->
                        new Post(
                                rs.getLong("id"),
                                rs.getString("title"),
                                rs.getString("content"),
                                rs.getBytes("image"),
                                rs.getTimestamp("created_at").toLocalDateTime(),
                                rs.getTimestamp("updated_at").toLocalDateTime()
                        ),
                limit,
                offset
        );
        int total = jdbcTemplate.queryForObject(COUNT_QUERY_ALL, Integer.class);
        return new PageImpl<>(posts, pageable, total);
    }

    @Override
    public void saveWithTags(Post post) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(SAVE_QUERY, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, post.getTitle());
                    ps.setString(2, post.getContent());
                    ps.setBytes(3, post.getImage());
                    return ps;
                },
                keyHolder
        );
        long postId = (long) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        jdbcTemplate.batchUpdate(
                POST_TAG_SAVE_QUERY,
                post.getTags(),
                100,
                (PreparedStatement ps, Tag tag) -> {
                    ps.setLong(1, postId);
                    ps.setLong(2, tag.getId());
                }
        );
    }

    @Override
    public Post getReferenceById(Long id) {
        return jdbcTemplate.queryForObject(
                FIND_BY_ID_QUERY,
                (rs, rowNum) ->
                        new Post(
                                rs.getLong("id"),
                                rs.getString("title"),
                                rs.getString("content"),
                                rs.getBytes("image"),
                                rs.getTimestamp("created_at").toLocalDateTime(),
                                rs.getTimestamp("updated_at").toLocalDateTime()
                        ),
                id
        );
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }

    @Override
    public void update(Post post) {
        jdbcTemplate.update(
                UPDATE_QUERY, post.getTitle(), post.getContent(), post.getImage(), post.getId()
        );
        jdbcTemplate.batchUpdate(
                POST_TAG_SAVE_QUERY,
                post.getTags(),
                100,
                (PreparedStatement ps, Tag tag) -> {
                    ps.setLong(1, post.getId());
                    ps.setLong(2, tag.getId());
                }
        );
    }
}