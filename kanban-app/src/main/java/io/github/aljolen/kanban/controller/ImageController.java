package io.github.aljolen.kanban.controller;

import io.github.aljolen.kanban.model.Image;
import io.github.aljolen.kanban.service.ImageService;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
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
            Optional<Image> optKanban = imageService.getImageById(id);
            if (optKanban.isPresent()) {
                return new ResponseEntity<>(
                        optKanban.get(),
                        HttpStatus.OK);
            } else {
                return noImageFoundResponse(id);
            }
        } catch (Exception e) {
            return errorResponse();
        }
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
        return new ResponseEntity<>("No kanban found with id: " + id, HttpStatus.NOT_FOUND);
    }
}
