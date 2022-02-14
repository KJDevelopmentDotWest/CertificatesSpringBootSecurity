package com.epam.esm.controller.controller;

import com.epam.esm.controller.ControllerTestConfig;
import com.epam.esm.controller.WebTestConfig;
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

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebTestConfig.class, ControllerTestConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebAppConfiguration
class CertificatesControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MockMvc mvc;

    private final String giftCertificateDtoSave = "{\"id\":null," +
            "\"name\":\"name\"," +
            "\"description\":\"description\"," +
            "\"price\":200.1," +
            "\"duration\":200," +
            "\"createDate\":[2022,1,3,15,4,46,402753000]," +
            "\"lastUpdateDate\":[2022,1,22,15,4,46,402753000]," +
            "\"tags\":[{\"id\":1,\"name\":\"firstTag\"}]}";

    private final String giftCertificateDtoUpdate = "{\"id\":10," +
            "\"name\":\"updated name\"," +
            "\"description\":\"description\"," +
            "\"price\":200.1," +
            "\"duration\":200," +
            "\"createDate\":[2022,1,3,15,4,46,402753000]," +
            "\"lastUpdateDate\":[2022,1,22,15,4,46,402753000]," +
            "\"tags\":[{\"id\":1,\"name\":\"firstTag\"}]}";

    private final String clearGiftCertificateTable = "DELETE FROM gift_certificate";
    private final String clearGiftCertificateToTagTable = "DELETE FROM gift_certificate_to_tag";
    private final String clearTagTable = "DELETE FROM tag";

    private final String insertGiftCertificate = """
            INSERT INTO gift_certificate(
            id, name, description, price, duration, create_date, last_update_date)
            VALUES (?, ?, ?, ?, ?, ?, ?)""";

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
        jdbcTemplate.update(clearGiftCertificateTable);
        jdbcTemplate.update(clearGiftCertificateToTagTable);
        jdbcTemplate.update(clearTagTable);

        jdbcTemplate.update(insertGiftCertificate, 1, "name1", "description1", 100, 30, "2002-10-20-12.00.00.000000", "2002-10-20-12.00.00.000000");
        jdbcTemplate.update(insertGiftCertificate, 2, "name2", "description2", 200, 15, "2002-10-20-12.00.00.000000", "2002-10-20-12.00.00.000000");

        jdbcTemplate.update(insertTag, 1, "tag1");
        jdbcTemplate.update(insertTag, 2, "tag2");

        jdbcTemplate.update(insertGiftCertificateToTag, 1, 1, 1);
        jdbcTemplate.update(insertGiftCertificateToTag, 2, 1, 2);
        jdbcTemplate.update(insertGiftCertificateToTag, 3, 2, 1);

    }

    @Test
    public void getByIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/certificates/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void getAllTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/certificates"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void getAllWithParametersTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/certificates?namePart=2&sortBy=date&ascending=false"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void deleteTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/certificates/10"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void saveTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(giftCertificateDtoSave))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/certificates/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(giftCertificateDtoUpdate))
                .andDo(MockMvcResultHandlers.print());
    }

}