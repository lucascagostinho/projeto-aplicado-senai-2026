import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Solicitacao } from './solicitacao.model';

@Injectable({ providedIn: 'root' })
export class SolicitacaoService {
  private readonly apiUrl = 'http://localhost:8080/api/solicitacoes';

  constructor(private http: HttpClient) {}

  listar(filtro?: { animalId?: number; adotanteId?: number }): Observable<Solicitacao[]> {
    let params = new HttpParams();
    if (filtro?.animalId) params = params.set('animalId', filtro.animalId);
    if (filtro?.adotanteId) params = params.set('adotanteId', filtro.adotanteId);
    return this.http.get<Solicitacao[]>(this.apiUrl, { params });
  }

  buscarPorId(id: number): Observable<Solicitacao> {
    return this.http.get<Solicitacao>(`${this.apiUrl}/${id}`);
  }

  criar(s: { animalId: number; adotanteId: number; mensagem?: string }): Observable<Solicitacao> {
    return this.http.post<Solicitacao>(this.apiUrl, s);
  }

  aprovar(id: number): Observable<Solicitacao> {
    return this.http.patch<Solicitacao>(`${this.apiUrl}/${id}/aprovar`, {});
  }

  recusar(id: number, justificativa: string): Observable<Solicitacao> {
    return this.http.patch<Solicitacao>(`${this.apiUrl}/${id}/recusar`, { justificativa });
  }

  cancelar(id: number, justificativa: string): Observable<Solicitacao> {
    return this.http.patch<Solicitacao>(`${this.apiUrl}/${id}/cancelar`, { justificativa });
  }
}
