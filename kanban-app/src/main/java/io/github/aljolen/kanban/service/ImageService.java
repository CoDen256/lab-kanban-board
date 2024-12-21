package io.github.aljolen.kanban.service;

import io.github.aljolen.kanban.model.Image;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    Image saveImage(MultipartFile image);
    Optional<Image> getImageById(UUID imageId);
}
