package io.github.aljolen.kanban.service;

import java.util.Optional;

public interface ImageService {
    String saveImage(String encodedImage);
    Optional<String> getImage(String imageId);
}
