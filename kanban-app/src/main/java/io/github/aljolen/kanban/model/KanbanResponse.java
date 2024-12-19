package io.github.aljolen.kanban.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        scope = KanbanResponse.class)
public class KanbanResponse extends Kanban {

    private List<Task> tasks = new ArrayList<>();

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public static KanbanResponse of(Kanban kanban, List<Task> tasks) {
        KanbanResponse response = new KanbanResponse();
        response.setId(kanban.getId());
        response.setTitle(kanban.getTitle());
        response.setTasks(tasks);
        return response;
    }
}
