package dev.ioannis.anemosparts.business;

import dev.ioannis.anemosparts.domain.Model;
import dev.ioannis.anemosparts.domain.Part;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface PartService {
    public List<Part> findAll();
    public List<Part> findByName(String name);
    public List<Part> findByModel(Model model);
    public Optional<Part> findById(long id);
    public Optional<Part> findByISBN(String ISBN);
    public Optional<Part> findByPartNumber(String partNumber);
    public Part save(Part part);
    public void deleteById(long id);
    public void delete(Part part);
}
