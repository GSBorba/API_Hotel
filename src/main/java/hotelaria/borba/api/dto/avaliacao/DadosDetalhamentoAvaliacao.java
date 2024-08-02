package hotelaria.borba.api.dto.avaliacao;

import hotelaria.borba.api.domain.Avaliacao;

public record DadosDetalhamentoAvaliacao(Long id, Integer nota, String descricao, Long id_reserva, String nome_cliente) {

    public DadosDetalhamentoAvaliacao(Avaliacao avaliacao) {
        this(avaliacao.getId(), avaliacao.getNota(), avaliacao.getDescricao(), avaliacao.getReserva().getId(), avaliacao.getReserva().getCliente().getNome());
    }
}
