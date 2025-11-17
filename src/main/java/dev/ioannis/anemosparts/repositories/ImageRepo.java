package dev.ioannis.anemosparts.repositories;

import java.io.IOException;
import java.io.InputStream;

public interface ImageRepo {
    String save(InputStream imageStream, String name) throws IOException;

    Boolean exists(String name);
}
