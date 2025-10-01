package dev.ioannis.anemosparts.business;

import dev.ioannis.anemosparts.domain.Model;
import dev.ioannis.anemosparts.domain.Part;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface PartService {
    public Set<Part> findAll();
    public Set<Part> findByName(String name);
    public Set<Part> findByModel(Model model);
    public Part findById(long id);
    public Part findByISBN(String ISBN);
    public Part findByPartNumber(String partNumber);
    public Part save(Part part);
    public long deleteById(long id);
    public long delete(Part part);
}
