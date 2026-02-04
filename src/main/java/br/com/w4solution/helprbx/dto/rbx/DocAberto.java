package br.com.w4solution.helprbx.dto.rbx;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DocAberto(
        @JsonProperty("Valor")
        Double valor
){
}
