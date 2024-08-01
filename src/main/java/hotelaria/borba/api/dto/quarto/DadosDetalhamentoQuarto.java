package hotelaria.borba.api.dto.quarto;

import hotelaria.borba.api.domain.Quarto;
import hotelaria.borba.api.dto.cama_quarto.DadosCadastroCamaQuarto;
import hotelaria.borba.api.dto.cama_quarto.DadosListagemCamaQuarto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public record DadosDetalhamentoQuarto(Long id, Integer numero, String tipo, BigDecimal preco_diaria, String descricao, String nome_hotel, List<DadosListagemCamaQuarto> camas) {

    public DadosDetalhamentoQuarto(Quarto quarto) {
        this(
                quarto.getId(),
                quarto.getNumero(),
                quarto.getTipoQuarto().getNomeTipo(),
                quarto.getPreco_diaria(),
                quarto.getDescricao(),
                quarto.getHotel().getNome(),
                quarto.getCamaQuarto().stream()
                        .map(DadosListagemCamaQuarto::new)
                        .collect(Collectors.toList())
        );
    }
}
