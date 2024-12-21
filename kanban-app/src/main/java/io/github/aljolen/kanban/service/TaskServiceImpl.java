package io.github.aljolen.kanban.service;

import io.github.aljolen.kanban.model.Task;
import io.github.aljolen.kanban.controller.TaskDTO;
import io.github.aljolen.kanban.model.TaskMessage;
import io.github.aljolen.kanban.repository.task.TaskRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ImageService imageService;

    public TaskServiceImpl(TaskRepository taskRepository, ImageService imageService) {
        this.taskRepository = taskRepository;
        this.imageService = imageService;
    }

    private TaskMessage mapTask(Task t) {
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
        return e;
    }

    @Override
    @Transactional
    public List<TaskMessage> getAllTasks() {
        System.out.println("getAllTasks invoked");
        List<TaskMessage> tasksList = new ArrayList<>();
        taskRepository.findAll().forEach(t -> tasksList.add(mapTask(t)));
        return tasksList;
    }

    @Override
    @Transactional
    public Optional<TaskMessage> getTaskById(String id) {
        return taskRepository.findById(id).map(this::mapTask);
    }

    @Override
    @Transactional
    public Optional<TaskMessage> getTaskByTitle(String title) {
        return taskRepository.findByTitle(title).map(this::mapTask);
    }


    @Override
    @Transactional
    public TaskMessage saveNewTask(TaskDTO taskDTO) {
        return mapTask(taskRepository.save(convertDTOToTask(taskDTO)));
    }

    @Override
    @Transactional
    public TaskMessage updateTask(Task oldTask, TaskDTO newTaskDTO) {
        return taskRepository.save(updateTaskFromDTO(oldTask, newTaskDTO));
    }

    @Override
    @Transactional
    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }

    private Task convertDTOToTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setColor(taskDTO.getColor());
        task.setStatus(taskDTO.getStatus());
        return task;
    }

    private TaskMessage updateTaskFromDTO(Task task, TaskDTO taskDTO){
        if(Optional.ofNullable(taskDTO.getTitle()).isPresent()){
            task.setTitle(taskDTO.getTitle());
        }

        if (Optional.ofNullable((taskDTO.getDescription())).isPresent()) {
            task.setDescription(taskDTO.getDescription());
        }

        if (Optional.ofNullable((taskDTO.getColor())).isPresent()) {
            task.setColor(taskDTO.getColor());
        }

        if (Optional.ofNullable((taskDTO.getStatus())).isPresent()) {
            task.setStatus(taskDTO.getStatus());
        }
        TaskMessage taskMessage = mapTask(task);
        if (taskDTO.getImage() != null) {
            taskMessage.setImage(taskDTO.getImage());
        }
        return taskMessage;
    }
}
