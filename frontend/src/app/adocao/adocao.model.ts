export interface Adocao {
  id?: number;
  solicitacaoId: number;
  animalId?: number;
  animalEspecie?: string;
  animalRaca?: string;
  confirmadoPorId: number;
  confirmadoPorNome?: string;
  dataAdocao: string;
  criadoEm?: string;
}
