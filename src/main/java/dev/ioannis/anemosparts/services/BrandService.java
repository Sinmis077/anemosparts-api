package dev.ioannis.anemosparts.services;

import dev.ioannis.anemosparts.domain.BrandDto;
import dev.ioannis.anemosparts.domain.requests.BrandSaveRequest;
import dev.ioannis.anemosparts.domain.responses.BrandFindAllResponse;
import org.springframework.web.client.HttpClientErrorException;

public interface BrandService {
    BrandFindAllResponse findAll();

    BrandDto save(BrandSaveRequest request) throws HttpClientErrorException.Conflict;

    BrandDto update(Long id, BrandSaveRequest request) throws HttpClientErrorException.Conflict;

    void delete(Long id) throws HttpClientErrorException.NotFound;
}
