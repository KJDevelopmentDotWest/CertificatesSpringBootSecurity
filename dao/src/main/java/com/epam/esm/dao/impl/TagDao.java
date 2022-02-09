package com.epam.esm.dao.impl;

import com.epam.esm.dao.api.Dao;
import com.epam.esm.dao.config.SpringConfig;
import com.epam.esm.dao.mapper.TagMapper;
import com.epam.esm.dao.model.tag.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Component
public class TagDao implements Dao<Tag> {

    private static final Logger logger = LogManager.getLogger(TagDao.class);

    private static final String SQL_SAVE_TAG = "INSERT INTO tag (name) VALUES (?)";

    private static final String SQL_UPDATE_TAG_BY_ID = "UPDATE tag SET name = ? WHERE id = ?";

    private static final String SQL_DELETE_TAG_BY_ID = "DELETE FROM tag WHERE id = ?";
    private static final String SQL_DELETE_GIFT_CERTIFICATE_TO_TAG_ENTRY_BY_TAG_ID = "DELETE FROM gift_certificate_to_tag WHERE tag_id = ?";

    private static final String SQL_FIND_ALL_TAGS = "SELECT id, name FROM tag";
    private static final String SQL_FIND_TAG_BY_ID = "SELECT id, name FROM tag WHERE id = ?";
    private static final String SQL_FIND_TAG_BY_NAME = "SELECT id, name FROM tag WHERE name = ?";

    //todo i don't know why autowired annotation doesn't work
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public TagDao() {
        jdbcTemplate = new SpringConfig().jdbcTemplate();
    }

    @Override
    public Tag saveEntity(Tag entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE_TAG, new String[] {"id"});
            preparedStatement.setString(1, entity.getName());
            return preparedStatement;
        }, keyHolder);

        //todo generated keys always null
        System.out.println(keyHolder.getKey());

        return findEntityById((Integer) keyHolder.getKey());
    }

    @Override
    public Boolean updateEntity(Tag entity) {
        if (Objects.nonNull(entity.getName())){
            return jdbcTemplate.update(SQL_UPDATE_TAG_BY_ID, entity.getName(), entity.getId(), entity.getName()) != 0;
        } else {
            return false;
        }
    }

    @Override
    public Boolean deleteEntity(Integer id) {
        jdbcTemplate.update(SQL_DELETE_GIFT_CERTIFICATE_TO_TAG_ENTRY_BY_TAG_ID, id);
        return jdbcTemplate.update(SQL_DELETE_TAG_BY_ID, id) != 0;
    }

    @Override
    public List<Tag> findAllEntities() {
        return jdbcTemplate.query(SQL_FIND_ALL_TAGS, new TagMapper());
    }

    @Override
    public Tag findEntityById(Integer id) {
        return jdbcTemplate.query(SQL_FIND_TAG_BY_ID, new TagMapper(), id).stream().findAny().orElse(null);
    }

    /**
     *
     * @param name name to be searched
     * @return tag with provided name, null otherwise
     */
    public Tag findTagByName(String name){
        return jdbcTemplate.query(SQL_FIND_TAG_BY_NAME, new TagMapper(), name).stream().findAny().orElse(null);
    }
}
