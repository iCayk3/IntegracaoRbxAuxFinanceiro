package br.com.w4solution.helprbx.services.rbx;

import br.com.w4solution.helprbx.dto.rbx.Atendimentos;
import br.com.w4solution.helprbx.dto.rbx.DocAberto;
import br.com.w4solution.helprbx.dto.rbx.ValorBaixado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class ServiceRbx {
    @Autowired
    private IntegracaoRbx integracaoRbx;
    @Value("${api.service.integration.rbx.chave}")
    private String chaveApi;


    public String taxaInadiplente(LocalDate data) {

        LocalDate dataFiltro = (data != null) ? data : LocalDate.now();
        var value = 0.0;

        String consultaInadiplentes = """
                    {
                       "ConsultaDocumentosAbertos": {
                              "Autenticacao": {
                                 "ChaveIntegracao": "EN6PYLEGXSV7LK3LL2YQ6R1E5U2LG2"
                              },
                        		 "Filtro": "Data >= '2026-01-01' AND Data <= '2026-01-31' AND Tipo = 'C'"
                
                           }
                    }
                """;

        String consultaDocBaixados = """
                {
                   "ConsultaDocumentosBaixados": {
                      "Autenticacao": {
                         "ChaveIntegracao": "EN6PYLEGXSV7LK3LL2YQ6R1E5U2LG2"
                      },
                      "Filtro": "Movimento.Data >= '2026-01-01' AND Movimento.Data <= '2026-01-31' AND Movimento.Tipo = 'C' AND Historicos.Descricao = 'Documento a receber'"
                   }
                }
                """;

        try {

            List<DocAberto> docAbertos = integracaoRbx.fazerRequest(
                    consultaInadiplentes,
                    new TypeReference<RespostaAPI<DocAberto>>() {
                    }
            );

            List<ValorBaixado> docBaixados = integracaoRbx.fazerRequest(
                    consultaDocBaixados,
                    new TypeReference<RespostaAPI<ValorBaixado>>() {
                    }
            );

            Double valorTotalDocAbertos = docAbertos.stream()
                    .map(DocAberto::valor)
                    .filter(Objects::nonNull)
                    .mapToDouble(Double::doubleValue)
                    .sum();

            Double valorTotalDocBaixados = docBaixados.stream()
                    .map(ValorBaixado::valor)
                    .filter(Objects::nonNull)
                    .mapToDouble(Double::doubleValue)
                    .sum();

            double percentualBaixado = 0.0;

            if ((valorTotalDocBaixados + valorTotalDocAbertos) > 0) {
                percentualBaixado = (valorTotalDocBaixados / (valorTotalDocBaixados + valorTotalDocAbertos)) * 100;
            }

            System.out.printf("""
                    Faturamento mês de janeiro: %.2f
                    Valores recebido: %.2f
                    Valor em atraso: %.2f
                    Porcentagem recebida: %.2f%%
                    %n""", valorTotalDocAbertos + valorTotalDocBaixados, valorTotalDocBaixados, valorTotalDocAbertos, percentualBaixado);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar integração com RBX: " + e.getMessage(), e);
        }

        return """
                
                """;
    }

    public String indiceRecuperacao(LocalDate data) {

        String consultarAtendimentoRecuperado = """
                    {
                       "ConsultaAtendimentos": {
                           "Autenticacao": {
                               "ChaveIntegracao": "EN6PYLEGXSV7LK3LL2YQ6R1E5U2LG2"
                           },
                           "Filtro": "AtendT.Nome = 'Negociação' AND AtendC.Nome = 'Reativação'"
                       }
                    }
                """;
        String consultarAtendimentoNRecuperado = """
                    {
                       "ConsultaAtendimentos": {
                           "Autenticacao": {
                               "ChaveIntegracao": "EN6PYLEGXSV7LK3LL2YQ6R1E5U2LG2"
                           },
                           "Filtro": "AtendT.Nome = 'Negociação' AND AtendC.Nome != 'Reativação'"
                       }
                    }
                """;

        try {
            List<Atendimentos> atendimentosRecuperados = integracaoRbx.fazerRequest(
                    consultarAtendimentoRecuperado,
                    new TypeReference<RespostaAPI<Atendimentos>>() {
                    }
            );

            List<Atendimentos> atendimentosNRecuperados = integracaoRbx.fazerRequest(
                    consultarAtendimentoNRecuperado,
                    new TypeReference<RespostaAPI<Atendimentos>>() {
                    }
            );

            System.out.println(atendimentosRecuperados.size());
            System.out.println(atendimentosNRecuperados.size());

            if(atendimentosRecuperados.equals(atendimentosNRecuperados)){
                System.out.println("e o mesmo");
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar integração com RBX: " + e.getMessage(), e);
        }

        return """
                """;
    }

}
