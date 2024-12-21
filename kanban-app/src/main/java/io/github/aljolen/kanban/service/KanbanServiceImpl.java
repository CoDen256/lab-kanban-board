package io.github.aljolen.kanban.service;

import io.github.aljolen.kanban.model.Kanban;
import io.github.aljolen.kanban.controller.KanbanDTO;
import io.github.aljolen.kanban.model.Task;
import io.github.aljolen.kanban.controller.TaskDTO;
import io.github.aljolen.kanban.model.TaskMessage;
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
    private final ImageService imageService;


    public KanbanServiceImpl(KanbanRepository kanbanRepository, TaskRepository taskRepository,
                             ImageService imageService) {
        this.kanbanRepository = kanbanRepository;
        this.taskRepository = taskRepository;
        this.imageService = imageService;
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
    public List<TaskMessage> getTasksByKanbanId(Long id) {
        List<TaskMessage> taskList = new ArrayList<>();
        taskRepository.findAllByKanbanId(id).forEach(t -> {
            TaskMessage e = new TaskMessage();
            e.setId(t.getId());
            e.setTitle(t.getTitle());
            e.setDescription(t.getDescription());
            e.setKanbanId(t.getKanbanId());
            e.setColor(t.getColor());
            e.setStatus(t.getStatus());
            e.setImageId(t.getImageId());
            if (t.getImageId() != null) {
                e.setImage(imageService.getImage(t.getImageId()).orElse(null));
            }
            taskList.add(e);
        });
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
        return kanbanRepository.save(kanban);
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
        if (taskDTO.getImage() != null) {
            task.setImageId(imageService.saveImage(taskDTO.getImage()));
        }
        return task;
    }
}
