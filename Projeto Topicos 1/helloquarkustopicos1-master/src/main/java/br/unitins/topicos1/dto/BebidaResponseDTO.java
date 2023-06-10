package br.unitins.topicos1.dto;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.unitins.topicos1.model.Bebida;
import br.unitins.topicos1.model.TipoBebida;

public record BebidaResponseDTO(
    Long id,
    String nome,
    Map<String, Object> marca,
    double valor,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String descricao,
    int estoque,
    TipoBebida tipoBebida,
    String nomeImagem
) {
    public BebidaResponseDTO(Bebida bebida) {
        this(bebida.getId(), bebida.getNome(), verMarca(bebida.getMarca().getNome()), bebida.getValor(), bebida.getDescricao(), bebida.getEstoque(), bebida.getTipoBebida(), bebida.getNomeImagem());
    }

    public static Map<String, Object> verMarca(String nome) {

        Map<String, Object> marca = new HashMap<>();

        marca.put("nome", nome);

        return marca;
    }

}
