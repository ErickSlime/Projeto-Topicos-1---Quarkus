package br.unitins.topicos1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EnderecoDTO(
    @NotBlank(message = "O campo logradouro não pode estar vazio")
    String logradouro,

    @NotBlank(message = "O campo bairro não pode estar vazio")
    String bairro,

    @NotBlank(message = "O campo cep não pode estar vazio")
    String cep,

    @NotBlank(message = "O campo número não pode estar vazio")
    String numero,

    String complemento,

    @NotNull
    Long idMunicipio
) {

}