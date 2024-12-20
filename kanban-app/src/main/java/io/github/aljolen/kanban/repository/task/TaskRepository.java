package io.github.aljolen.kanban.repository.task;

import io.github.aljolen.kanban.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends MongoRepository<Task, Long> {

    Optional<Task> findByTitle(String title);

    Iterable<Task> findAllByKanbanId(Long kanbanId);
}
