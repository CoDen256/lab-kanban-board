package io.github.aljolen.kanban.service;

import io.github.aljolen.kanban.model.Kanban;
import io.github.aljolen.kanban.controller.KanbanDTO;
import io.github.aljolen.kanban.model.Task;
import io.github.aljolen.kanban.controller.TaskDTO;
import io.github.aljolen.kanban.repository.kanban.KanbanRepository;

import io.github.aljolen.kanban.repository.task.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class KanbanServiceImpl implements KanbanService {

    private final TaskRepository taskRepository;
    private final KanbanRepository kanbanRepository;


    public KanbanServiceImpl(KanbanRepository kanbanRepository, TaskRepository taskRepository) {
        this.kanbanRepository = kanbanRepository;
        this.taskRepository = taskRepository;
    }


    @Override
    @Transactional
    public List<Kanban> getAllKanbanBoards() {
        List<Kanban> kanbanList = new ArrayList<>();
        kanbanRepository.findAll().forEach(kanbanList::add);
        return kanbanList;
    }

    @Override
    @Transactional
    public Optional<Kanban> getKanbanById(Long id) {
        return kanbanRepository.findById(id);
    }

    @Override
    public List<Task> getTasksByKanbanId(Long id) {
        List<Task> taskList = new ArrayList<>();
        taskRepository.findAllByKanbanId(id).forEach(taskList::add);
        return taskList;
    }

    @Override
    @Transactional
    public Optional<Kanban> getKanbanByTitle(String title) {
        return kanbanRepository.findByTitle(title);
    }

    @Override
    @Transactional
    public Kanban saveNewKanban(KanbanDTO kanbanDTO) {
        return kanbanRepository.save(convertDTOToKanban(kanbanDTO));
    }

    @Override
    @Transactional
    public Kanban updateKanban(Kanban oldKanban, KanbanDTO newKanbanDTO) {
        oldKanban.setTitle(newKanbanDTO.getTitle());
        return kanbanRepository.save(oldKanban);
    }

    @Override
    @Transactional
    public void deleteKanban(Kanban kanban) {
        kanbanRepository.delete(kanban);
    }

    @Override
    @Transactional
    public Kanban addNewTaskToKanban(Long kanbanId, TaskDTO taskDTO) {
        Kanban kanban = kanbanRepository.findById(kanbanId).get();
        taskRepository.save(convertDTOToTask(taskDTO, kanbanId));
        return kanban;
    }

    private Kanban convertDTOToKanban(KanbanDTO kanbanDTO){
        Kanban kanban = new Kanban();
        kanban.setTitle(kanbanDTO.getTitle());
        return kanban;
    }

    private Task convertDTOToTask(TaskDTO taskDTO, Long kanbanId) {
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setColor(taskDTO.getColor());
        task.setStatus(taskDTO.getStatus());
        task.setKanbanId(kanbanId);
        if (taskDTO.getImageId() != null) {
            task.setImageId(taskDTO.getImageId());
        }
        return task;
    }
}
