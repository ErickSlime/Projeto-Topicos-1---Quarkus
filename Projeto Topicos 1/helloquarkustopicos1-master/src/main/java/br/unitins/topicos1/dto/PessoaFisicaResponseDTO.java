package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.PessoaFisica;
import br.unitins.topicos1.model.Sexo;

public record PessoaFisicaResponseDTO(
        Long id,
        String nome,
        String cpf,
        String email,
        Sexo sexo) {
    public PessoaFisicaResponseDTO(PessoaFisica pessoaFisica) {
        this(pessoaFisica.getId(),pessoaFisica.getNome(), pessoaFisica.getCpf(), pessoaFisica.getEmail(), pessoaFisica.getSexo());
    }
}