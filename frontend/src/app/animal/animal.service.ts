import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Animal } from './animal.model';

@Injectable({ providedIn: 'root' })
export class AnimalService {

  private readonly apiUrl = 'http://localhost:8080/api/animais';

  constructor(private http: HttpClient) {}

  listar(): Observable<Animal[]> {
    return this.http.get<Animal[]>(this.apiUrl);
  }

  buscarPorId(id: number): Observable<Animal> {
    return this.http.get<Animal>(`${this.apiUrl}/${id}`);
  }

  criar(animal: Animal): Observable<Animal> {
    return this.http.post<Animal>(this.apiUrl, animal);
  }

  atualizar(id: number, animal: Animal): Observable<Animal> {
    return this.http.put<Animal>(`${this.apiUrl}/${id}`, animal);
  }

  deletar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
