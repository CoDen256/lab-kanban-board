package io.github.aljolen.kanban.service;

import io.github.aljolen.kanban.model.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public class ImageLocalRepository implements CrudRepository<Image, UUID> {

    private static final String UPLOAD_DIR = "/images/";


    private <S extends Image> S saveLocally(S image) throws IOException {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        if (image.getId() == null) {
            image.setId(UUID.randomUUID());
        }
        String filename = image.getId() + "_" + image.getName();
        Files.write(uploadDir.toPath().resolve(filename), image.getImage());
        return image;
    }


    private Optional<Image> readLocally(UUID id) throws IOException {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            return Optional.empty();
        }
        File file = findFileByPrefix(id.toString());
        if (file == null) {return Optional.empty();}
        String[] s = file.getName().split("_", 2);
        Image value = new Image();
        value.setId(UUID.fromString(s[0]));
        value.setName(s[1]);
        value.setImage(Files.readAllBytes(file.toPath()));
        return Optional.of(value);
    }

    private static File findFileByPrefix(String prefix) {
        File uploadDir = new File(UPLOAD_DIR);

        // Ensure directory exists
        if (!uploadDir.exists() || !uploadDir.isDirectory()) {
            throw new IllegalArgumentException("Upload directory does not exist.");
        }

        // Find the file with the specified prefix
        File[] matchingFiles = uploadDir.listFiles((dir, name) -> name.startsWith(prefix));

        return (matchingFiles != null && matchingFiles.length > 0) ? matchingFiles[0] : null;
    }


    @Override
    public <S extends Image> S save(S entity) {
        try {
            return saveLocally(entity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <S extends Image> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Image> findById(UUID uuid) {
        try {
            return readLocally(uuid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsById(UUID uuid) {
        return false;
    }

    @Override
    public Iterable<Image> findAll() {
        return null;
    }

    @Override
    public Iterable<Image> findAllById(Iterable<UUID> uuids) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(UUID uuid) {

    }

    @Override
    public void delete(Image entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends UUID> uuids) {

    }

    @Override
    public void deleteAll(Iterable<? extends Image> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
