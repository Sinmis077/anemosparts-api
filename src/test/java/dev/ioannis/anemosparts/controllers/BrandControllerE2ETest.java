package dev.ioannis.anemosparts.controllers;

import dev.ioannis.anemosparts.repositories.BrandRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BrandControllerE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BrandRepo brandRepo;

    @BeforeEach
    void setUp() {
        brandRepo.deleteAll();
    }

    @Test
    void findAll_returnsEmptyList_whenNoBrandsExist() throws Exception {
        mockMvc.perform(get("/api/brands"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brands", hasSize(0)));
    }

    @Test
    void findAll_returnsBrands_whenBrandsExist() throws Exception {
        mockMvc.perform(post("/api/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Kawasaki\",\"iconUrl\":\"http://example.com/kawasaki.png\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/brands"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brands", hasSize(1)))
                .andExpect(jsonPath("$.brands[0].name", is("Kawasaki")))
                .andExpect(jsonPath("$.brands[0].iconUrl", is("http://example.com/kawasaki.png")));
    }

    @Test
    void create_returnsBrand_whenValidRequest() throws Exception {
        mockMvc.perform(post("/api/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Kawasaki\",\"iconUrl\":\"http://example.com/kawasaki.png\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Kawasaki")))
                .andExpect(jsonPath("$.iconUrl", is("http://example.com/kawasaki.png")));
    }

    @Test
    void create_returnsError_whenNameIsNull() throws Exception {
        // Note: Spring validation should return 400, but verify actual behavior
        mockMvc.perform(post("/api/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"iconUrl\":\"http://example.com/kawasaki.png\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_succeeds_whenIconUrlIsNull() throws Exception {
        mockMvc.perform(post("/api/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Kawasaki\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Kawasaki")))
                .andExpect(jsonPath("$.iconUrl", nullValue()));
    }

    @Test
    void update_returnsUpdatedBrand_whenBrandExists() throws Exception {
        String createResponse = mockMvc.perform(post("/api/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Kawasaki\",\"iconUrl\":\"http://example.com/old.png\"}"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long brandId = Long.parseLong(createResponse.split("\"id\":")[1].split(",")[0]);

        mockMvc.perform(put("/api/brands/" + brandId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Kawasaki Racing\",\"iconUrl\":\"http://example.com/new.png\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(brandId.intValue())))
                .andExpect(jsonPath("$.name", is("Kawasaki Racing")))
                .andExpect(jsonPath("$.iconUrl", is("http://example.com/new.png")));
    }

    @Test
    void delete_succeeds_whenBrandExists() throws Exception {
        String createResponse = mockMvc.perform(post("/api/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Kawasaki\"}"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long brandId = Long.parseLong(createResponse.split("\"id\":")[1].split(",")[0]);

        mockMvc.perform(delete("/api/brands/" + brandId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/brands"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brands", hasSize(0)));
    }

    @Test
    void delete_returnsInternalServerError_whenBrandDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/brands/999"))
                .andExpect(status().isNotFound());
    }
}