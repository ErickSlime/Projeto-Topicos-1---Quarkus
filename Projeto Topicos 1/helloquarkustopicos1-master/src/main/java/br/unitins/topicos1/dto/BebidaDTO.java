package br.unitins.topicos1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BebidaDTO(

    @NotBlank(message = "O campo nome deve ser informado.")
    String nome,

    @NotNull(message = "O campo idMarca deve ser informado.")
    Long idmarca,

    double valor,

    @NotBlank(message = "O campo descrição deve ser informado.")
    @Size(max = 120, message = "Maximo de 120 caracteres")
    String descricao,

    Integer tipoBebida,

    int estoque
) {
  
}
