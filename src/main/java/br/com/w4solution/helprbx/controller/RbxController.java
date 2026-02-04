package br.com.w4solution.helprbx.controller;


import br.com.w4solution.helprbx.dto.rbx.ConsuFaturamentoDto;
import br.com.w4solution.helprbx.dto.rbx.FaturamentoDto;
import br.com.w4solution.helprbx.services.rbx.ServiceRbx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rbx")
public class RbxController {

    @Autowired
    ServiceRbx service;

    @PostMapping("/faturamento")
    public ResponseEntity<FaturamentoDto> consultarTaxaInadiplente(@RequestBody(required = false) ConsuFaturamentoDto dados){

        var faturamento = service.taxaInadiplente(dados.data());

        return ResponseEntity.ok().body(faturamento);
    }

    @PostMapping("/recuperado")
    public ResponseEntity<?> consultarAtendimentoRecuperado(){

        service.indiceRecuperacao(null);

        return ResponseEntity.ok().build();
    }

}
