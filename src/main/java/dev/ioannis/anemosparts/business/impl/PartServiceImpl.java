package dev.ioannis.anemosparts.business.impl;

import dev.ioannis.anemosparts.business.PartService;
import dev.ioannis.anemosparts.domain.Model;
import dev.ioannis.anemosparts.domain.Part;
import dev.ioannis.anemosparts.domain.entity.PartEntity;
import dev.ioannis.anemosparts.mapper.PartMapper;
import dev.ioannis.anemosparts.persistance.PartRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

@Service
@AllArgsConstructor
public class PartServiceImpl implements PartService {
    @Override
    public Set<Part> findAll() {
        Set<Part> parts = new HashSet<>();
        for(PartEntity entity : partRepo.findAll()){
            parts.add(PartMapper.INSTANCE.toProduct(entity));
        }

//        parts.add(new Part(1, "test", "test", "12344131231", "2131", 2, 1, new HashSet<>()));

        return parts;
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

    private PartRepo partRepo;


}
