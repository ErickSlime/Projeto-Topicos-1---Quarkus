package br.unitins.topicos1.dto;

import jakarta.validation.constraints.NotBlank;

public record TelefoneDTO(
    @NotBlank(message = "O campo código de área não pode ser nulo")
    String codigoArea,

    @NotBlank(message = "O campo código de área não pode ser nulo")
    String numero
) {
    
}