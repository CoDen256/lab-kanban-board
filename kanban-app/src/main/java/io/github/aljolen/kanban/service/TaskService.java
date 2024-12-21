package io.github.aljolen.kanban.service;

import io.github.aljolen.kanban.model.Task;
import io.github.aljolen.kanban.controller.TaskDTO;

import io.github.aljolen.kanban.model.TaskMessage;
import java.util.List;
import java.util.Optional;

public interface TaskService {

    List<TaskMessage> getAllTasks();

    Optional<TaskMessage> getTaskById(String id);

    Optional<TaskMessage> getTaskByTitle(String title);

    TaskMessage saveNewTask(TaskDTO taskDTO);

    TaskMessage updateTask(Task oldTask, TaskDTO newTaskDTO);

    void deleteTask(Task task);
}
