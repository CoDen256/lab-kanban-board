package io.github.aljolen.kanban.controller;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.github.aljolen.kanban.model.Kanban;
import io.github.aljolen.kanban.model.Task;
import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        scope = KanbanMessage.class)
public class KanbanMessage extends Kanban {

    private List<Task> tasks = new ArrayList<>();

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public static KanbanMessage of(Kanban kanban, List<Task> tasks) {
        KanbanMessage response = new KanbanMessage();
        response.setId(kanban.getId());
        response.setTitle(kanban.getTitle());
        response.setTasks(tasks);
        return response;
    }
}
