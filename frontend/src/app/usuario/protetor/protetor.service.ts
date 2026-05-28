import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Protetor } from './protetor.model';

@Injectable({ providedIn: 'root' })
export class ProtetorService {
  private readonly apiUrl = 'http://localhost:8080/api/protetores';

  constructor(private http: HttpClient) {}

  listar(): Observable<Protetor[]> { return this.http.get<Protetor[]>(this.apiUrl); }
  buscarPorId(id: number): Observable<Protetor> { return this.http.get<Protetor>(`${this.apiUrl}/${id}`); }
  criar(p: Protetor): Observable<Protetor> { return this.http.post<Protetor>(this.apiUrl, p); }
  atualizar(id: number, p: Protetor): Observable<Protetor> { return this.http.put<Protetor>(`${this.apiUrl}/${id}`, p); }
  deletar(id: number): Observable<void> { return this.http.delete<void>(`${this.apiUrl}/${id}`); }
}
