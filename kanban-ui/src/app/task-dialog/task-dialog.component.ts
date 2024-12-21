import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialog } from '@angular/material';
import { Task } from '../model/task/task';
import { MatInputModule } from '@angular/material/input';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { KanbanService } from '../service/kanban-service.service';
import { TaskService } from '../service/task.service';

@Component({
  selector: 'app-task-dialog',
  templateUrl: './task-dialog.component.html',
  styleUrls: ['./task-dialog.component.css']
})
export class TaskDialogComponent implements OnInit {

  dialogTitle: String;
  kanbanId: String;
  task: Task;
  selectedImage: File | null = null;
  imagePreviewUrl: string | null = null; // URL for preview
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<TaskDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data,
    private kanbanService: KanbanService,
    private taskService: TaskService) {

    this.dialogTitle = data.title;
    this.kanbanId = data.kanbanId;
    this.task = data.task;

    this.form = fb.group({
      title: [this.task.title, Validators.required],
      description: [this.task.description, Validators.required],
      color: [this.task.color,Validators.required]
  });
   }

  uploadFile(event: Event): void {
    const fileInput = event.target as HTMLInputElement;
    if (fileInput.files && fileInput.files[0]) {
      this.selectedImage = fileInput.files[0]; // Store the selected file
      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreviewUrl = reader.result as string;
        console.log('Selected url:', this.imagePreviewUrl);
      };
      reader.readAsDataURL(this.selectedImage);
      console.log('Selected image:', this.selectedImage);
    }
  }

  ngOnInit() {
  }

  save() {
    this.mapFormToTaskModel();
    if (!this.task.id) {
      console.log("saving in kanban")
      this.kanbanService.saveNewTaskInKanban(this.kanbanId, this.task, this.selectedImage)
        .subscribe(
          (res) => console.log(res),
          (err) => console.log(err)
        );
    } else {
      this.taskService.updateTask(this.task).subscribe();
    }
    this.dialogRef.close();
    window.location.reload();
  }

  close() {
      this.dialogRef.close();
  }

  private mapFormToTaskModel(): void {
    this.task.title = this.form.get('title').value;
    this.task.description = this.form.get('description').value;
    this.task.color = this.form.get('color').value;
    this.task.status = 'TODO';
  }

}
