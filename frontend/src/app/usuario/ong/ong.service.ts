import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Ong } from './ong.model';

@Injectable({ providedIn: 'root' })
export class OngService {
  private readonly apiUrl = 'http://localhost:8080/api/ongs';

  constructor(private http: HttpClient) {}

  listar(): Observable<Ong[]> {
    return this.http.get<Ong[]>(this.apiUrl);
  }

  buscarPorId(id: number): Observable<Ong> {
    return this.http.get<Ong>(`${this.apiUrl}/${id}`);
  }

  criar(ong: Ong): Observable<Ong> {
    return this.http.post<Ong>(this.apiUrl, ong);
  }

  atualizar(id: number, ong: Ong): Observable<Ong> {
    return this.http.put<Ong>(`${this.apiUrl}/${id}`, ong);
  }

  deletar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
