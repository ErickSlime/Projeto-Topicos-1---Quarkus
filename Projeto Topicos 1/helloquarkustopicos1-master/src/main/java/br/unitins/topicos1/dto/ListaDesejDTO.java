package br.unitins.topicos1.dto;

import jakarta.validation.constraints.NotNull;

public record ListaDesejDTO(
    @NotNull
    Long idUsuario,

    Long idProduto
) {
    
}