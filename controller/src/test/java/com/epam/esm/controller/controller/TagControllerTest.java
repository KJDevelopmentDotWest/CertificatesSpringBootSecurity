package com.epam.esm.controller.controller;

import com.epam.esm.controller.ControllerTestConfig;
import com.epam.esm.controller.WebTestConfig;
import com.epam.esm.controller.config.WebConfig;
import com.epam.esm.dao.impl.GiftCertificateDao;
import com.epam.esm.dao.impl.TagDao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebTestConfig.class, ControllerTestConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebAppConfiguration
class TagControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MockMvc mvc;

    private String tagDtoSave = "{\"id\":1,\"name\":\"fiEited\"}";

    private final String clearTagTable = "DELETE FROM tag";
    private final String clearGiftCertificateToTagTable = "DELETE FROM gift_certificate_to_tag";

    private final String insertTag = """
            INSERT INTO tag(
            id, name)
            VALUES (?, ?)""";

    private final String insertGiftCertificateToTag = """
            INSERT INTO gift_certificate_to_tag(
            id, gift_certificate_id, tag_id)
            VALUES (?, ?, ?)""";

    @BeforeAll
    public void setup(){
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        jdbcTemplate.update(clearGiftCertificateToTagTable);
        jdbcTemplate.update(clearTagTable);

        jdbcTemplate.update(insertTag, 1, "tag1");
        jdbcTemplate.update(insertTag, 2, "tag2");

        jdbcTemplate.update(insertGiftCertificateToTag, 1, 1, 1);
        jdbcTemplate.update(insertGiftCertificateToTag, 2, 1, 2);
        jdbcTemplate.update(insertGiftCertificateToTag, 3, 2, 1);
    }

    @Test
    public void getByIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/tags/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void getAllTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/tags"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void deleteTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/tags/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void saveTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tagDtoSave))
                .andDo(MockMvcResultHandlers.print());
    }

}