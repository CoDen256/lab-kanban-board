package io.github.aljolen.kanban.service;

import io.github.aljolen.kanban.model.Image;
import io.github.aljolen.kanban.repository.kanban.ImageRepository;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public Image saveImage(MultipartFile image) {
        Image newImage = new Image();
        try {
            newImage.setName(image.getOriginalFilename());
            newImage.setImage(image.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return imageRepository.save(newImage);
    }

    @Override
    public Optional<Image> getImageById(UUID imageId) {
        return imageRepository.findById(imageId);
    }

    private byte[] decode(String encodedImage) {
        return Base64.decodeBase64(encodedImage.getBytes());
    }

    private ImageType getImageType(String encodedImage) {
        String type = encodedImage.split(";")[0];
        return switch (type) {
            case "data:image/jpg", "data:image/jpeg" -> ImageType.JPG;
            case "data:image/png" -> ImageType.PNG;
            case "data:image/gif" -> ImageType.GIF;
            default -> null;
        };
    }

    enum ImageType {
        PNG, JPG, GIF
    }
}
