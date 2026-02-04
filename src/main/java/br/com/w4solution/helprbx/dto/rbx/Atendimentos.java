package br.com.w4solution.helprbx.dto.rbx;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Atendimentos(
        @JsonProperty("Protocolo")
        String protocolo
) {
}
