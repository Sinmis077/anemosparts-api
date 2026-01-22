package dev.ioannis.anemosparts.controllers;

import dev.ioannis.anemosparts.entities.Brand;
import dev.ioannis.anemosparts.helpers.AuthTokenHelper;
import dev.ioannis.anemosparts.repositories.BrandRepo;
import dev.ioannis.anemosparts.repositories.ModelRepo;
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
class ModelControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelRepo modelRepo;

    @Autowired
    private BrandRepo brandRepo;

    private Long brandId;
    @Autowired
    private AuthTokenHelper authTokenHelper;

    @BeforeEach
    void setUp() {
        modelRepo.deleteAll();
        brandRepo.deleteAll();

        Brand brand = new Brand();
        brand.setName("Kawasaki");
        brand.setIconUrl("http://example.com/kawasaki.png");
        brandId = brandRepo.save(brand).getId();
    }

    @Test
    void findAll_returnsEmptyList_whenNoModelsExist() throws Exception {
        mockMvc.perform(get("/api/models"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.models", hasSize(0)));
    }

    @Test
    void findAll_returnsModels_whenModelsExist() throws Exception {
        mockMvc.perform(post("/api/models")
                        .cookie(authTokenHelper.adminCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"ZX-10R\",\"productionYear\":2020,\"brandId\":" + brandId + "}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/models"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.models", hasSize(1)))
                .andExpect(jsonPath("$.models[0].name", is("ZX-10R")))
                .andExpect(jsonPath("$.models[0].productionYear", is(2020)));
    }

    @Test
    void create_returnsModel_whenValidRequest() throws Exception {
        mockMvc.perform(post("/api/models")
                        .cookie(authTokenHelper.adminCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"ZX-10R\",\"productionYear\":2020,\"brandId\":" + brandId + "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("ZX-10R")))
                .andExpect(jsonPath("$.productionYear", is(2020)));
    }

    @Test
    void create_returnsBadRequest_whenNameIsNull() throws Exception {
        mockMvc.perform(post("/api/models")
                        .cookie(authTokenHelper.adminCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productionYear\":2020,\"brandId\":" + brandId + "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_returnsBadRequest_whenProductionYearIsTooOld() throws Exception {
        mockMvc.perform(post("/api/models")
                        .cookie(authTokenHelper.adminCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"ZX-10R\",\"productionYear\":1800,\"brandId\":" + brandId + "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_returnsBadRequest_whenProductionYearIsTooNew() throws Exception {
        mockMvc.perform(post("/api/models")
                        .cookie(authTokenHelper.adminCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"ZX-10R\",\"productionYear\":2030,\"brandId\":" + brandId + "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_returnsNotFound_whenBrandDoesNotExist() throws Exception {
        mockMvc.perform(post("/api/models")
                        .cookie(authTokenHelper.adminCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"ZX-10R\",\"productionYear\":2020,\"brandId\":999}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_returnsUpdatedModel_whenModelExists() throws Exception {
        String createResponse = mockMvc.perform(post("/api/models")
                        .cookie(authTokenHelper.adminCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"ZX-10R\",\"productionYear\":2020,\"brandId\":" + brandId + "}"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long modelId = Long.parseLong(createResponse.split("\"id\":")[1].split(",")[0]);

        mockMvc.perform(put("/api/models/" + modelId)
                        .cookie(authTokenHelper.adminCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"ZX-10RR\",\"productionYear\":2021,\"brandId\":" + brandId + "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(modelId.intValue())))
                .andExpect(jsonPath("$.name", is("ZX-10RR")))
                .andExpect(jsonPath("$.productionYear", is(2021)));
    }

    @Test
    void update_returnsNotFound_whenBrandDoesNotExist() throws Exception {
        String createResponse = mockMvc.perform(post("/api/models")
                        .cookie(authTokenHelper.adminCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"ZX-10R\",\"productionYear\":2020,\"brandId\":" + brandId + "}"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long modelId = Long.parseLong(createResponse.split("\"id\":")[1].split(",")[0]);

        mockMvc.perform(put("/api/models/" + modelId)
                        .cookie(authTokenHelper.adminCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"ZX-10RR\",\"productionYear\":2021,\"brandId\":999}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_succeeds_whenModelExists() throws Exception {
        String createResponse = mockMvc.perform(post("/api/models")
                        .cookie(authTokenHelper.adminCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"ZX-10R\",\"productionYear\":2020,\"brandId\":" + brandId + "}"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long modelId = Long.parseLong(createResponse.split("\"id\":")[1].split(",")[0]);

        mockMvc.perform(delete("/api/models/" + modelId)
                        .cookie(authTokenHelper.adminCookie())
                ).andExpect(status().isNoContent());

        mockMvc.perform(get("/api/models"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.models", hasSize(0)));
    }

    @Test
    void delete_returnsNotFound_whenModelDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/models/999")
                        .cookie(authTokenHelper.adminCookie())
                ).andExpect(status().isNotFound());
    }
}