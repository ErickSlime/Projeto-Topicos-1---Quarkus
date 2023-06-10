package br.unitins.topicos1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioDTO(

                @NotBlank(message = "O campo login deve ser informado.") String login,

                @NotBlank(message = "O campo senha deve ser informado.") String senha,

                @NotNull(message = "O campo telefone deve ser informado.") TelefoneDTO telefone,

                @NotNull(message = "O campo endereco deve ser informado.") EnderecoDTO endereco,

                @NotNull(message = "O campo pessoa deve ser informado.") PessoaFisicaDTO pessoa) {

}
