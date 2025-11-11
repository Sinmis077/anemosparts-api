package dev.ioannis.anemosparts.services;

import java.security.NoSuchAlgorithmException;

public interface SecurityService {
    String bytesToHash(byte[] bytes) throws NoSuchAlgorithmException;
}
