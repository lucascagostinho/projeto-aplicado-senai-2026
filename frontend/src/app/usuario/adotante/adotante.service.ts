import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Adotante } from './adotante.model';

@Injectable({ providedIn: 'root' })
export class AdotanteService {
  private readonly apiUrl = 'http://localhost:8080/api/adotantes';

  constructor(private http: HttpClient) {}

  listar(): Observable<Adotante[]> { return this.http.get<Adotante[]>(this.apiUrl); }
  buscarPorId(id: number): Observable<Adotante> { return this.http.get<Adotante>(`${this.apiUrl}/${id}`); }
  criar(a: Adotante): Observable<Adotante> { return this.http.post<Adotante>(this.apiUrl, a); }
  atualizar(id: number, a: Adotante): Observable<Adotante> { return this.http.put<Adotante>(`${this.apiUrl}/${id}`, a); }
  deletar(id: number): Observable<void> { return this.http.delete<void>(`${this.apiUrl}/${id}`); }
}
