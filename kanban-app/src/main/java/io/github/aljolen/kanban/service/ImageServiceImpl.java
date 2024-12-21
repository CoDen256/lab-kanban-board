package io.github.aljolen.kanban.service;

import io.github.aljolen.kanban.model.Image;
import io.github.aljolen.kanban.repository.kanban.ImageRepository;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final ImageLocalRepository imageLocalRepository = new ImageLocalRepository();

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
        MediaType imageType = getMediaTypeForFileName(newImage.getName());
        if (imageType == MediaType.IMAGE_GIF){
            return imageLocalRepository.save(newImage);
        }else {
            return imageRepository.save(newImage);
        }
    }

    @Override
    public Optional<Image> getImageById(UUID imageId) {
        if (imageRepository.existsById(imageId)) {
            return imageRepository.findById(imageId);
        }
        return imageLocalRepository.findById(imageId);
    }


    private static final Map<String, MediaType> MEDIA_TYPE_MAP = new HashMap<>();

    static {
        MEDIA_TYPE_MAP.put("jpg", MediaType.IMAGE_JPEG);
        MEDIA_TYPE_MAP.put("jpeg", MediaType.IMAGE_JPEG);
        MEDIA_TYPE_MAP.put("png", MediaType.IMAGE_PNG);
        MEDIA_TYPE_MAP.put("gif", MediaType.IMAGE_GIF);
    }

    public static MediaType getMediaTypeForFileName(String fileName) {
        String extension = getFileExtension(fileName).toLowerCase();
        return MEDIA_TYPE_MAP.getOrDefault(extension, MediaType.APPLICATION_OCTET_STREAM);
    }

    private static String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return (lastDot != -1 && lastDot < fileName.length() - 1) ? fileName.substring(lastDot + 1) : "";
    }
}
