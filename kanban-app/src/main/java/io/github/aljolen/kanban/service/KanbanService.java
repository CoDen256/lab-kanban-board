package io.github.aljolen.kanban.service;

import io.github.aljolen.kanban.model.Kanban;
import io.github.aljolen.kanban.controller.KanbanDTO;
import io.github.aljolen.kanban.model.Task;
import io.github.aljolen.kanban.controller.TaskDTO;

import io.github.aljolen.kanban.model.TaskMessage;
import java.util.List;
import java.util.Optional;

public interface KanbanService {

    List<Kanban> getAllKanbanBoards();

    Optional<Kanban> getKanbanById(Long id);

    List<TaskMessage> getTasksByKanbanId(Long id);

    Optional<Kanban> getKanbanByTitle(String title);

    Kanban saveNewKanban(KanbanDTO kanbanDTO);

    Kanban updateKanban(Kanban oldKanban, KanbanDTO newKanbanDTO);

    void deleteKanban(Kanban kanban);

    Kanban addNewTaskToKanban(Long kanbanId, TaskDTO taskDTO);
}
