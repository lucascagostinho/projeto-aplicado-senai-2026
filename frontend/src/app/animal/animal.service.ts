import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Animal, AnimalFiltro } from './animal.model';

@Injectable({ providedIn: 'root' })
export class AnimalService {

  private readonly apiUrl = 'http://localhost:8080/api/animais';

  constructor(private http: HttpClient) {}

  listar(filtro?: AnimalFiltro): Observable<Animal[]> {
    let params = new HttpParams();
    if (filtro) {
      if (filtro.especie)        params = params.set('especie',     filtro.especie);
      if (filtro.porte)          params = params.set('porte',       filtro.porte);
      if (filtro.faixaEtaria)    params = params.set('faixaEtaria', filtro.faixaEtaria);
      if (filtro.sexo)           params = params.set('sexo',        filtro.sexo);
      if (filtro.cidade?.trim()) params = params.set('cidade',      filtro.cidade!.trim());
      if (filtro.status)         params = params.set('status',      filtro.status);
    }
    return this.http.get<Animal[]>(this.apiUrl, { params });
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
