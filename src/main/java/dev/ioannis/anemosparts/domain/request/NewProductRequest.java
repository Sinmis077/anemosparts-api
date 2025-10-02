package dev.ioannis.anemosparts.domain.request;

import java.util.Set;

public class NewProductRequest {
    private long id;
    private String name;
    private String description;
    private double price;
    private int quantity;

    private Set<Long> model;

}
