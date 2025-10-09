package dev.ioannis.anemosparts.business.impl;

import dev.ioannis.anemosparts.business.PartService;
import dev.ioannis.anemosparts.domain.Model;
import dev.ioannis.anemosparts.domain.Part;
import dev.ioannis.anemosparts.mapper.ModelMapper;
import dev.ioannis.anemosparts.mapper.PartMapper;
import dev.ioannis.anemosparts.persistance.PartRepo;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PartServiceImpl implements PartService {

    private final PartRepo partRepo;
    @Override
    public List<Part> findAll() {
        return PartMapper.INSTANCE.toModels(IterableUtils.toList(partRepo.findAll()));
    }

    @Override
    public List<Part> findByName(String name) {
        return PartMapper.INSTANCE.toModels(partRepo.findByName(name));
    }

    @Override
    public List<Part> findByModel(Model model) {
        return PartMapper.INSTANCE.toModels(partRepo.findByModels(ModelMapper.INSTANCE.toEntity(model).getId()));
    }

    @Override
    public Optional<Part> findById(long id) {
        return Optional.ofNullable(PartMapper.INSTANCE.toModel(partRepo.findById(id).orElse(null)));
    }

    @Override
    public Optional<Part> findByISBN(String ISBN) {
        return Optional.ofNullable(PartMapper.INSTANCE.toModel(partRepo.findByIsbn(ISBN).orElse(null)));
    }

    @Override
    public Optional<Part> findByPartNumber(String partNumber) {
        return Optional.ofNullable(PartMapper.INSTANCE.toModel(partRepo.findByPartNumber(partNumber).orElse(null)));
    }

    @Override
    public Part save(Part part) {
        return PartMapper.INSTANCE.toModel(partRepo.save(PartMapper.INSTANCE.toEntity(part)));
    }

    @Override
    public void deleteById(long id) {
        try {
            partRepo.deleteById(id);
        }
        catch (Exception e) {
            throw new ServiceException("Failed to delete part with id " + id);
        }
    }

    @Override
    public void delete(Part part) {
        try {
            partRepo.delete(PartMapper.INSTANCE.toEntity(part));
        }
        catch (Exception e) {
            throw new ServiceException("Failed to delete part");
        }
    }
}
