package br.unitins.topicos1.dto;

import jakarta.validation.constraints.NotBlank;

public record MarcaDTO (

@NotBlank(message = "O campo nome deve ser informado.")
String nome,

@NotBlank(message = "O campo cnpj deve ser informado.")
String cnpj

) {

}