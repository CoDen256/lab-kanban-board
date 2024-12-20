package io.github.aljolen.kanban.controller;

import io.github.aljolen.kanban.model.Kanban;
import io.github.aljolen.kanban.model.KanbanDTO;
import io.github.aljolen.kanban.model.KanbanResponse;
import io.github.aljolen.kanban.model.TaskDTO;
import io.github.aljolen.kanban.service.KanbanService;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kanbans")
public class KanbanController {

    public KanbanController(KanbanService kanbanService) {
        this.kanbanService = kanbanService;
    }

    private final KanbanService kanbanService;

    @GetMapping("/")
    public ResponseEntity<?> getAllKanbans(){
        try {
            return new ResponseEntity<>(
                    kanbanService.getAllKanbanBoards(),
                    HttpStatus.OK);
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getKanban(@PathVariable Long id){
        try {
            Optional<Kanban> optKanban = kanbanService.getKanbanById(id);
            if (optKanban.isPresent()) {
                return new ResponseEntity<>(
                        KanbanResponse.of(optKanban.get(), kanbanService.getTasksByKanbanId(id)),
                        HttpStatus.OK);
            } else {
                return noKanbanFoundResponse(id);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getKanbanByTitle(@RequestParam String title){
        try {
            Optional<Kanban> optKanban = kanbanService.getKanbanByTitle(title);
            if (optKanban.isPresent()) {
                return new ResponseEntity<>(
                        KanbanResponse.of(optKanban.get(), kanbanService.getTasksByKanbanId(optKanban.get().getId())),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No kanban found with a title: " + title, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createKanban(@RequestBody KanbanDTO kanbanDTO){
        try {
            return new ResponseEntity<>(
                    KanbanResponse.of(kanbanService.saveNewKanban(kanbanDTO), new ArrayList<>()),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateKanban(@PathVariable Long id, @RequestBody KanbanDTO kanbanDTO){
        try {
            Optional<Kanban> optKanban = kanbanService.getKanbanById(id);
            if (optKanban.isPresent()) {
                Kanban kanban = kanbanService.updateKanban(optKanban.get(), kanbanDTO);
                return new ResponseEntity<>(
                        KanbanResponse.of(kanban, kanbanService.getTasksByKanbanId(kanban.getId())),
                        HttpStatus.OK);
            } else {
                return noKanbanFoundResponse(id);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteKanban(@PathVariable Long id){
        try {
            Optional<Kanban> optKanban = kanbanService.getKanbanById(id);
            if (optKanban.isPresent()) {
                kanbanService.deleteKanban(optKanban.get());
                return new ResponseEntity<>(
                        String.format("Kanban with id: %d was deleted", id),
                        HttpStatus.OK);
            } else {
                return noKanbanFoundResponse(id);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @GetMapping("/{kanbanId}/tasks/")
    public ResponseEntity<?> getAllTasksInKanban(@PathVariable Long kanbanId){
         try {
            Optional<Kanban> optKanban = kanbanService.getKanbanById(kanbanId);
            if (optKanban.isPresent()) {
                return new ResponseEntity<>(
                        kanbanService.getTasksByKanbanId(kanbanId),
                        HttpStatus.OK);
            } else {
                return noKanbanFoundResponse(kanbanId);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @PostMapping("/{kanbanId}/tasks/")
    public ResponseEntity<?> createTaskAssignedToKanban(@PathVariable Long kanbanId, @RequestBody TaskDTO taskDTO){
        try {
            return new ResponseEntity<>(
                    KanbanResponse.of(kanbanService.addNewTaskToKanban(kanbanId, taskDTO), kanbanService.getTasksByKanbanId(kanbanId)),

                    HttpStatus.CREATED);
        } catch (Exception e) {
            return errorResponse();
        }
    }

    private ResponseEntity<String> errorResponse(){
        return new ResponseEntity<>("Something went wrong :(", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<String> noKanbanFoundResponse(Long id){
        return new ResponseEntity<>("No kanban found with id: " + id, HttpStatus.NOT_FOUND);
    }
}
