package dev.ioannis.anemosparts.business.impl;

import dev.ioannis.anemosparts.business.PartService;
import dev.ioannis.anemosparts.domain.Model;
import dev.ioannis.anemosparts.domain.Part;
import dev.ioannis.anemosparts.mapper.PartMapper;
import dev.ioannis.anemosparts.persistance.PartRepo;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

@Service
@AllArgsConstructor
public class PartServiceImpl implements PartService {

    private final PartRepo partRepo;
    @Override
    public Set<Part> findAll() {
        return PartMapper.INSTANCE.toProducts(new HashSet<>(IterableUtils.toList(partRepo.findAll())));
    }

    @Override
    public Set<Part> findByName(String name) {
        Set<Part> parts = new HashSet<>();
        for(Part part : findAll()) {
            if(part.getName().equals(name)){
                parts.add(part);
            }
        }
        return  parts;
    }

    @Override
    public Set<Part> findByModel(Model model) {
        Set<Part> parts = new HashSet<>();
        for(Part part : findAll()) {
            for (Model modelIndex : part.getModels()) {
                if(Objects.equals(modelIndex, model)){
                    parts.add(part);
                }
            }
        }
        return parts;
    }

    @Override
    public Part findById(long id) {
        return PartMapper.INSTANCE.toProduct(partRepo.findById(id).orElseThrow(NoSuchElementException::new));
    }

    @Override
    public Part findByISBN(String ISBN) {
        return null;
    }

    @Override
    public Part findByPartNumber(String partNumber) {
        return null;
    }

    @Override
    public Part save(Part part) {
        return null;
    }

    @Override
    public long deleteById(long id) {
        return 0;
    }

    @Override
    public long delete(Part part) {
        return 0;
    }
}
