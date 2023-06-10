package br.unitins.topicos1.dto;

import jakarta.validation.constraints.NotNull;

public record CompletarCadastroDTO (
    
@NotNull(message = "O campo telefone deve ser informado.") TelefoneDTO telefone,

@NotNull(message = "O campo endereco deve ser informado.") EnderecoDTO endereco,

@NotNull(message = "O campo pessoa deve ser informado.") PessoaFisicaDTO pessoa)
{
    
}
