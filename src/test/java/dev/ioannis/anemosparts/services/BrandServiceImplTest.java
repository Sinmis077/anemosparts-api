package dev.ioannis.anemosparts.services;

import dev.ioannis.anemosparts.domain.BrandDto;
import dev.ioannis.anemosparts.domain.requests.BrandSaveRequest;
import dev.ioannis.anemosparts.entities.Brand;
import dev.ioannis.anemosparts.mappers.BrandMapper;
import dev.ioannis.anemosparts.repositories.BrandRepo;
import dev.ioannis.anemosparts.services.impl.BrandServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BrandServiceImplTest {

    @Mock
    private BrandRepo brandRepo;
    @Mock
    private BrandMapper brandMapper;

    @InjectMocks
    private BrandServiceImpl brandService;

    private BrandSaveRequest request;
    private Brand brand;
    private BrandDto brandDto;

    @BeforeEach
    void setUp() {
        request = new BrandSaveRequest();
        request.setName("Kawasaki");
        request.setIconUrl("https://example.com/kawasaki.png");

        brand = new Brand();
        brand.setId(1L);
        brand.setName("Kawasaki");
        brand.setIconUrl("https://example.com/kawasaki.png");

        brandDto = new BrandDto();
        brandDto.setId(1L);
        brandDto.setName("Kawasaki");
        brandDto.setIconUrl("https://example.com/kawasaki.png");
    }

    @Test
    void findAll_returnsEmptyList_whenNoBrandsExist() {
        when(brandRepo.findAll()).thenReturn(List.of());

        var result = brandService.findAll();

        assertTrue(result.brands().isEmpty());
        verify(brandRepo).findAll();
    }

    @Test
    void findAll_returnsBrands_whenBrandsExist() {
        when(brandRepo.findAll()).thenReturn(List.of(brand));
        when(brandMapper.toDto(brand)).thenReturn(brandDto);

        var result = brandService.findAll();

        assertEquals(1, result.brands().size());
        verify(brandRepo).findAll();
    }

    @Test
    void save_returnsSavedBrand() {
        when(brandRepo.save(any(Brand.class))).thenReturn(brand);
        when(brandMapper.toDto(brand)).thenReturn(brandDto);

        var result = brandService.save(request);

        assertEquals("Kawasaki", result.getName());
        verify(brandRepo).save(any(Brand.class));
    }

    @Test
    void update_returnsUpdatedBrand() {
        when(brandRepo.save(any(Brand.class))).thenReturn(brand);
        when(brandMapper.toDto(brand)).thenReturn(brandDto);

        var result = brandService.update(1L, request);

        assertEquals("Kawasaki", result.getName());
        verify(brandRepo).save(any(Brand.class));
    }

    @Test
    void delete_succeeds_whenBrandExists() {
        when(brandRepo.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> brandService.delete(1L));
        verify(brandRepo).deleteById(1L);
    }

    @Test
    void delete_throwsException_whenBrandDoesNotExist() {
        when(brandRepo.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> brandService.delete(1L));
        verify(brandRepo, never()).deleteById(any());
    }
}