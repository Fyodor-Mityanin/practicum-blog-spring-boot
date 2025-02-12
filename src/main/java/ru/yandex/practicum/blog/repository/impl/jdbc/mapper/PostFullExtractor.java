package ru.yandex.practicum.blog.repository.impl.jdbc.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.blog.domain.Post;
import ru.yandex.practicum.blog.domain.Tag;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostFullExtractor implements ResultSetExtractor<List<Post>> {

    @Override
    public List<Post> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Post> postMap = new HashMap<>();

        while (rs.next()) {
            Long postId = rs.getLong("id");
            Post post = postMap.get(postId);
            if (post == null) {
                post = new Post();
                post.setId(postId);
                post.setTitle(rs.getString("title"));
                post.setContent(rs.getString("content"));
                post.setImage(rs.getBytes("image"));
                post.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                post.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                postMap.put(postId, post);
            }
            long tagId = rs.getLong("tag_id");
            if (tagId != 0) {
                Tag tag = new Tag();
                tag.setId(tagId);
                tag.setName(rs.getString("tag_name"));
                post.getTags().add(tag);
            }
        }

        return new ArrayList<>(postMap.values());
    }
}
