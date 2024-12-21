package io.github.aljolen.kanban.repository.kanban;

import io.github.aljolen.kanban.model.TaskImage;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends CrudRepository<TaskImage, UUID> {
}
