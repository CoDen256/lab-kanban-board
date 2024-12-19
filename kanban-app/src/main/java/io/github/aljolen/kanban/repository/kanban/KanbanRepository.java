package io.github.aljolen.kanban.repository.kanban;

import io.github.aljolen.kanban.model.Kanban;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KanbanRepository extends CrudRepository<Kanban, Long> {

    Optional<Kanban> findByTitle(String title);
}
