package dev.ioannis.anemosparts.services.impl;

import dev.ioannis.anemosparts.domain.BrandDto;
import dev.ioannis.anemosparts.domain.requests.BrandSaveRequest;
import dev.ioannis.anemosparts.domain.responses.BrandFindAllResponse;
import dev.ioannis.anemosparts.entities.Brand;
import dev.ioannis.anemosparts.mappers.BrandMapper;
import dev.ioannis.anemosparts.repositories.BrandRepo;
import dev.ioannis.anemosparts.services.BrandService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class BrandServiceImpl implements BrandService {
    private final BrandRepo brandRepo;
    private final BrandMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public BrandFindAllResponse findAll() {
        return new BrandFindAllResponse(brandRepo.findAll().stream().map(mapper::toDto).toList());
    }

    @Override
    public BrandDto save(BrandSaveRequest request) {
        var brand = new Brand();
        brand.setName(request.getName());
        brand.setIconUrl(request.getIconUrl());

        return mapper.toDto(save(brand));
    }

    @Override
    public BrandDto update(Long id, BrandSaveRequest request) {
        var brand = new Brand();
        brand.setId(id);
        brand.setName(request.getName());
        brand.setIconUrl(request.getIconUrl());

        return mapper.toDto(save(brand));
    }

    protected Brand save(Brand brand) {
        return brandRepo.save(brand);
    }

    @Override
    public void delete(Long id) {
        if(!brandRepo.existsById(id)) {
            throw new EntityNotFoundException("Brand with id " + id + " does not exist");
        }

        brandRepo.deleteById(id);
    }
}
