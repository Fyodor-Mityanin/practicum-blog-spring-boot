package ru.yandex.practicum.blog.repository.impl.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.blog.domain.Tag;
import ru.yandex.practicum.blog.repository.TagRepository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {
    public static final String FIND_BY_NAME_QUERY = """
            SELECT t.*
            FROM tags t
            WHERE LOWER(t.name) IN (%s)
            """;
    public static final String FIND_BY_POST_QUERY = """
            SELECT t.*, pt.post_id as post_id
            FROM tags t
            JOIN post_tags pt ON pt.tag_id = t.id
            WHERE pt.post_id IN (%s)
            """;
    public static final String SAVE_QUERY = "INSERT INTO tags (name) VALUES (?)";

    private final JdbcTemplate jdbcTemplate;

    public List<Tag> findByNameInIgnoreCase(Collection<String> names) {
        String inSql = String.join(",", Collections.nCopies(names.size(), "?"));
        return jdbcTemplate.query(
                String.format(FIND_BY_NAME_QUERY, inSql),
                (rs, rowNum) -> new Tag(
                        rs.getLong("id"),
                        rs.getString("name")
                ),
                names.toArray()
        );
    }

    @Override
    public List<Tag> save(List<Tag> tags) {
        for (Tag tag : tags) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(SAVE_QUERY, Statement.RETURN_GENERATED_KEYS);
                        ps.setString(1, tag.getName());
                        return ps;
                    },
                    keyHolder
            );
            long tagId = (long) Objects.requireNonNull(keyHolder.getKeys()).get("id");
            tag.setId(tagId);
        }
        return tags;
    }


    public List<Tag> getOrCreate(Set<String> tags) {
        List<Tag> existedTags = this.findByNameInIgnoreCase(tags);
        Set<String> existedTagNames = existedTags.stream().map(Tag::getName).collect(Collectors.toSet());
        List<Tag> newTags = new ArrayList<>();
        tags.forEach(t -> {
            if (!existedTagNames.contains(t)) {
                Tag tag = new Tag();
                tag.setName(t);
                newTags.add(tag);
            }
        });
        this.save(newTags);
        newTags.addAll(existedTags);
        return newTags;
    }

    @Override
    public List<Tag> findAll() {
        return jdbcTemplate.query(
                "select id, name from tags",
                (rs, rowNum) -> new Tag(
                        rs.getLong("id"),
                        rs.getString("name")
                )
        );
    }

    @Override
    public List<Tag> getTagsByPostIds(List<Long> postIds) {
        String inSql = String.join(",", Collections.nCopies(postIds.size(), "?"));
        return jdbcTemplate.query(
                String.format(FIND_BY_POST_QUERY, inSql),
                (rs, rowNum) -> new Tag(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getLong("post_id")
                ),
                postIds.toArray()
        );
    }
}
