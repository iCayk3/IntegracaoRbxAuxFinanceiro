package br.com.w4solution.helprbx.dto.rbx;

public record FaturamentoDto(
        Double faturamento,
        Double recebido,
        Double nRecebido,
        Double porcentagemRecebida
) {
}
