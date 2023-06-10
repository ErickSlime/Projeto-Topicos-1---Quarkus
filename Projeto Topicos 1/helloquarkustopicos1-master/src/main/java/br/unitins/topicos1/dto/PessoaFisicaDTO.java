package br.unitins.topicos1.dto;

import jakarta.validation.constraints.NotBlank;

public record PessoaFisicaDTO(

        @NotBlank(message = "O campo cnpj deve ser informado.") String nome,

        @NotBlank(message = "O campo cnpj deve ser informado.") String cpf,

        @NotBlank(message = "O campo cnpj deve ser informado.") String email,

        Integer sexo

) {

}