import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import { Kanban } from '../model/kanban/kanban';
import { Task } from '../model/task/task';
import { environment } from 'src/environments/environment';
import {Image} from "../model/image/task";

@Injectable({
  providedIn: 'root'
})
export class KanbanService {

  private kanbanAppUrl = environment.kanbanAppUrl

  constructor(private http: HttpClient) { }

  retrieveAllKanbanBoards(): Observable<Kanban[]> {
    return this.http.get<Kanban[]>(this.kanbanAppUrl + '/kanbans/');
  }

  retrieveKanbanById(id: String): Observable<Kanban> {
    return this.http.get<Kanban>(this.kanbanAppUrl + '/kanbans/' + id);
  }

  saveNewKanban(title: string): Observable<string> {
    let headers = new HttpHeaders({'Content-Type': 'application/json' });
    let options = { headers: headers };
    let jsonObject = this.prepareTiTleJsonObject(title);
    return this.http.post<string>(
      this.kanbanAppUrl + '/kanbans/',
      jsonObject,
      options
    );
  }

  saveNewTaskInKanban(kanbanId: String, task: Task): Promise<Task> {
    console.log("Saving task")
    console.log(task)
    return fetch(this.kanbanAppUrl + '/kanbans/' + kanbanId + '/tasks/', {
      method: 'POST',
      body: JSON.stringify(task),
      headers: {'Content-Type': 'application/json' }
    })
      .then(response => response.json())
      .then(data => {
        console.log("Got task", data)
        return data
      })
      .catch(error => console.error("Error occurred:", error));
  }

  getImageUrl(id: String): string{
    return this.kanbanAppUrl +"/images/"+id
  }

  getImageById(id: string): Promise<void|Blob>{
    return fetch(this.kanbanAppUrl +"/images/"+id)
      .then(r => r.blob())
      .then(data => {
        console.log("Got blob", data)
        return data
      })
      .catch(error => console.error("Error occurred:", error));
  }

  saveImage(file: File): Promise<Image> {
    const formData = new FormData();
    formData.append('image', file, file.name);

    return fetch(this.kanbanAppUrl + "/images/", {
      method: 'POST',
      body: formData,
    })
      .then(response => response.json())
      .then(data => {
        console.log("Got image", data)
        return data
      })
      .catch(error => console.error("Error occurred:", error));
  }

  private prepareTiTleJsonObject(title: string) {
    const object = {
      title: title
    }
    return JSON.stringify(object);
  }

}
