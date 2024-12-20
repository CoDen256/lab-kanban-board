package io.github.aljolen.kanban.service;

import io.github.aljolen.kanban.model.Task;
import io.github.aljolen.kanban.model.TaskDTO;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    List<Task> getAllTasks();

    Optional<Task> getTaskById(String id);

    Optional<Task> getTaskByTitle(String title);

    Task saveNewTask(TaskDTO taskDTO);

    Task updateTask(Task oldTask, TaskDTO newTaskDTO);

    void deleteTask(Task task);
}
