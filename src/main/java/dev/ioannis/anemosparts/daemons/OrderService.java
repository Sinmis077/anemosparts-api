package dev.ioannis.anemosparts.daemons;

public interface OrderService {
    boolean isRequestPossible(OrderRequest request);

    void placeOrder(OrderRequest request);
}
