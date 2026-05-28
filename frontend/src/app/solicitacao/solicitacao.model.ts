export type SolicitacaoStatus = 'PENDENTE' | 'APROVADA' | 'RECUSADA' | 'CANCELADA';

export interface Solicitacao {
  id?: number;
  animalId: number;
  animalEspecie?: string;
  animalRaca?: string;
  adotanteId: number;
  adotanteNome?: string;
  status: SolicitacaoStatus;
  mensagem?: string;
  justificativa?: string;
  criadoEm?: string;
  atualizadoEm?: string;
}
