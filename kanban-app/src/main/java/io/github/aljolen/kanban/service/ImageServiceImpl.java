package io.github.aljolen.kanban.service;

import io.github.aljolen.kanban.model.TaskImage;
import io.github.aljolen.kanban.repository.kanban.ImageRepository;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public String saveImage(String encodedImage) {
        TaskImage image = new TaskImage();
        image.setImage(decode(encodedImage));
        return imageRepository.save(image).getId().toString();
    }

    @Override
    public Optional<String> getImage(String imageId) {
        return imageRepository.findById(UUID.fromString(imageId))
                .map(TaskImage::getImage)
                .map(Base64::encodeBase64String);
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
