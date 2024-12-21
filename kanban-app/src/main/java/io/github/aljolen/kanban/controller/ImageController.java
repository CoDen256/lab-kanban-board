package io.github.aljolen.kanban.controller;

import io.github.aljolen.kanban.model.Image;
import io.github.aljolen.kanban.service.ImageService;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/images")
public class ImageController{

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }


    @GetMapping("/")
    public ResponseEntity<?> getImages() {
        try {

            return new ResponseEntity<>(
                        "{'message': 'ok'}",
                        HttpStatus.OK);

        } catch (Exception e) {
            return errorResponse();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getImage(@PathVariable UUID id) {
        try {
            Optional<Image> image = imageService.getImageById(id);
            if (image.isPresent()) {
                return ResponseEntity.ok()
                        .contentType(getMediaTypeForFileName(image.get().getName()))
                        .body(image.get().getImage());
            } else {
                return noImageFoundResponse(id);
            }
        } catch (Exception e) {
            return errorResponse();
        }
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

    @PostMapping("/")
    public ResponseEntity<?> createImage(@RequestParam("image") MultipartFile file) {
        try {
            Image saved = imageService.saveImage(file);
            return new ResponseEntity<>(
                    new ImageDTO(saved.getId(),saved.getName()),
                    HttpStatus.OK);
        } catch (Exception e) {
            return errorResponse();
        }
    }

    record ImageDTO(UUID id, String name) {}


    private ResponseEntity<String> errorResponse() {
        return new ResponseEntity<>("Something went wrong :(", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<String> noImageFoundResponse(UUID id) {
        return new ResponseEntity<>("No image found with id: " + id, HttpStatus.NOT_FOUND);
    }
}
