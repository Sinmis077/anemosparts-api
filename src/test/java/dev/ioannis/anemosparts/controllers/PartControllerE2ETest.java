package dev.ioannis.anemosparts.controllers;

import dev.ioannis.anemosparts.entities.Brand;
import dev.ioannis.anemosparts.entities.Model;
import dev.ioannis.anemosparts.helpers.AuthTokenHelper;
import dev.ioannis.anemosparts.repositories.BrandRepo;
import dev.ioannis.anemosparts.repositories.ModelRepo;
import dev.ioannis.anemosparts.repositories.PartRepo;
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
class PartControllerE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PartRepo partRepo;

    @Autowired
    private ModelRepo modelRepo;

    @Autowired
    private BrandRepo brandRepo;

    private Long modelId;
    @Autowired
    private AuthTokenHelper authTokenHelper;

    @BeforeEach
    void setUp() {
        partRepo.deleteAll();
        modelRepo.deleteAll();
        brandRepo.deleteAll();

        Brand brand = new Brand();
        brand.setName("Kawasaki");
        brand.setIconUrl("http://example.com/kawasaki.png");
        Brand savedBrand = brandRepo.save(brand);

        Model model = new Model();
        model.setName("ZX-10R");
        model.setProductionYear(2020);
        model.setBrand(savedBrand);
        modelId = modelRepo.save(model).getId();
    }

    @Test
    void findAll_returnsEmptyList_whenNoPartsExist() throws Exception {
        mockMvc.perform(get("/api/parts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parts", hasSize(0)));
    }

    @Test
    void findAll_returnsParts_whenPartsExist() throws Exception {
        mockMvc.perform(post("/api/parts")
                        .cookie(authTokenHelper.adminCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Front Brake Pads\",\"description\":\"High performance brake pads for ZX-10R\",\"partNumber\":\"BP-ZX10R-F\",\"price\":75.50,\"quantity\":10,\"modelIds\":[" + modelId + "]}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/parts/full"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parts", hasSize(1)))
                .andExpect(jsonPath("$.parts[0].name", is("Front Brake Pads")))
                .andExpect(jsonPath("$.parts[0].partNumber", is("BP-ZX10R-F")));
    }

    @Test
    void findAllFull_returnsEmptyList_whenNoPartsExist() throws Exception {
        mockMvc.perform(get("/api/parts/full"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parts", hasSize(0)));
    }

    @Test
    void findAllFull_returnsParts_whenPartsExist() throws Exception {
        mockMvc.perform(post("/api/parts")
                        .cookie(authTokenHelper.adminCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Front Brake Pads\",\"description\":\"High performance brake pads for ZX-10R\",\"partNumber\":\"BP-ZX10R-F\",\"price\":75.50,\"quantity\":10,\"modelIds\":[" + modelId + "]}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/parts/full"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parts", hasSize(1)))
                .andExpect(jsonPath("$.parts[0].name", is("Front Brake Pads")));
    }

    @Test
    void create_returnsPart_whenValidRequest() throws Exception {
        mockMvc.perform(post("/api/parts")
                        .cookie(authTokenHelper.adminCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Front Brake Pads\",\"description\":\"High performance brake pads for ZX-10R\",\"partNumber\":\"BP-ZX10R-F\",\"price\":75.50,\"quantity\":10,\"modelIds\":[" + modelId + "]}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Front Brake Pads")))
                .andExpect(jsonPath("$.description", is("High performance brake pads for ZX-10R")))
                .andExpect(jsonPath("$.partNumber", is("BP-ZX10R-F")))
                .andExpect(jsonPath("$.price", is(75.50)))
                .andExpect(jsonPath("$.quantity", is(10)));
    }

    @Test
    void create_returnsBadRequest_whenNameIsBlank() throws Exception {
        mockMvc.perform(post("/api/parts")
                        .cookie(authTokenHelper.adminCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"description\":\"High performance brake pads\",\"partNumber\":\"BP-ZX10R-F\",\"price\":75.50,\"quantity\":10,\"modelIds\":[" + modelId + "]}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_returnsBadRequest_whenDescriptionIsTooShort() throws Exception {
        mockMvc.perform(post("/api/parts")
                        .cookie(authTokenHelper.adminCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Brake Pads\",\"description\":\"Short\",\"partNumber\":\"BP-ZX10R-F\",\"price\":75.50,\"quantity\":10,\"modelIds\":[" + modelId + "]}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_returnsBadRequest_whenPriceIsTooLow() throws Exception {
        mockMvc.perform(post("/api/parts")
                        .cookie(authTokenHelper.adminCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Brake Pads\",\"description\":\"High performance brake pads for ZX-10R\",\"partNumber\":\"BP-ZX10R-F\",\"price\":0.10,\"quantity\":10,\"modelIds\":[" + modelId + "]}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_returnsBadRequest_whenModelIdsAreEmpty() throws Exception {
        mockMvc.perform(post("/api/parts")
                        .cookie(authTokenHelper.adminCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Brake Pads\",\"description\":\"High performance brake pads for ZX-10R\",\"partNumber\":\"BP-ZX10R-F\",\"price\":75.50,\"quantity\":10,\"modelIds\":[]}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_succeeds_whenOptionalFieldsAreNull() throws Exception {
        mockMvc.perform(post("/api/parts")
                        .cookie(authTokenHelper.adminCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Brake Pads\",\"description\":\"High performance brake pads for ZX-10R\",\"partNumber\":\"BP-ZX10R-F\",\"price\":75.50,\"quantity\":10,\"modelIds\":[" + modelId + "]}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.oemNumber", nullValue()));
    }

    @Test
    void update_returnsUpdatedPart_whenPartExists() throws Exception {
        String createResponse = mockMvc.perform(post("/api/parts")
                        .cookie(authTokenHelper.adminCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Brake Pads\",\"description\":\"High performance brake pads for ZX-10R\",\"partNumber\":\"BP-ZX10R-F\",\"price\":75.50,\"quantity\":10,\"modelIds\":[" + modelId + "]}"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long partId = Long.parseLong(createResponse.split("\"id\":")[1].split(",")[0]);

        mockMvc.perform(put("/api/parts/" + partId)
                        .cookie(authTokenHelper.adminCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Premium Brake Pads\",\"description\":\"Ultra high performance racing brake pads designed specifically for Kawasaki ZX-10R motorcycles\",\"partNumber\":\"BP-ZX10R-F-PRO\",\"price\":125.00,\"quantity\":5,\"modelIds\":[" + modelId + "]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(partId.intValue())))
                .andExpect(jsonPath("$.name", is("Premium Brake Pads")))
                .andExpect(jsonPath("$.price", is(125.00)))
                .andExpect(jsonPath("$.quantity", is(5)));
    }

    @Test
    void delete_succeeds_whenPartExists() throws Exception {
        String createResponse = mockMvc.perform(post("/api/parts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(authTokenHelper.adminCookie())
                        .content("{\"name\":\"Brake Pads\",\"description\":\"High performance brake pads for ZX-10R\",\"partNumber\":\"BP-ZX10R-F\",\"price\":75.50,\"quantity\":10,\"modelIds\":[" + modelId + "]}"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long partId = Long.parseLong(createResponse.split("\"id\":")[1].split(",")[0]);

        mockMvc.perform(delete("/api/parts/" + partId)
                        .cookie(authTokenHelper.adminCookie())
                ).andExpect(status().isNoContent());

        mockMvc.perform(get("/api/parts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parts", hasSize(0)));
    }

    @Test
    void delete_returnsNotFound_whenPartDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/parts/999")
                        .cookie(authTokenHelper.adminCookie())
                ).andExpect(status().isNotFound());
    }
}