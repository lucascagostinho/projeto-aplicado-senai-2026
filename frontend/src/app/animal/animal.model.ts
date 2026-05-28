export type Especie = 'CAO' | 'GATO';
export type Sexo = 'MACHO' | 'FEMEA';
export type FaixaEtaria = 'FILHOTE' | 'JOVEM' | 'ADULTO' | 'SENIOR';
export type Porte = 'PEQUENO' | 'MEDIO' | 'GRANDE';
export type AnimalStatus = 'DISPONIVEL' | 'EM_PROCESSO' | 'ADOTADO' | 'INDISPONIVEL';

export interface Animal {
  id?: number;
  especie: Especie;
  raca?: string;
  sexo: Sexo;
  faixaEtaria: FaixaEtaria;
  porte: Porte;
  cor?: string;
  caracteristicas?: string;
  status: AnimalStatus;
  foto?: string;
  cidade: string;
  estado: string;
  castrado: boolean;
  vacinado: boolean;
  responsavelId?: number | null;
  responsavelNome?: string;
  responsavelTipo?: string;
  criadoEm?: string;
}

export interface AnimalFiltro {
  especie?: string | null;
  porte?: string | null;
  faixaEtaria?: string | null;
  sexo?: string | null;
  cidade?: string | null;
  status?: string | null;
}
