package io.github.aljolen.kanban.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        scope = KanbanMessage.class)
public class KanbanMessage extends Kanban {

    private List<TaskMessage> tasks = new ArrayList<>();

    public List<TaskMessage> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskMessage> tasks) {
        this.tasks = tasks;
    }

    public static KanbanMessage of(Kanban kanban, List<TaskMessage> tasks) {
        KanbanMessage response = new KanbanMessage();
        response.setId(kanban.getId());
        response.setTitle(kanban.getTitle());
        response.setTasks(tasks);
        return response;
    }
}
