import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Adocao } from './adocao.model';

@Injectable({ providedIn: 'root' })
export class AdocaoService {
  private readonly apiUrl = 'http://localhost:8080/api/adocoes';

  constructor(private http: HttpClient) {}

  listar(): Observable<Adocao[]> { return this.http.get<Adocao[]>(this.apiUrl); }
  buscarPorId(id: number): Observable<Adocao> { return this.http.get<Adocao>(`${this.apiUrl}/${id}`); }
  confirmar(a: { solicitacaoId: number; confirmadoPorId: number; dataAdocao: string }): Observable<Adocao> {
    return this.http.post<Adocao>(this.apiUrl, a);
  }
}
