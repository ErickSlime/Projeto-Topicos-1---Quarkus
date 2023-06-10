package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.Endereco;
import br.unitins.topicos1.model.PessoaFisica;
import br.unitins.topicos1.model.Telefone;
import br.unitins.topicos1.model.Usuario;

public record UsuarioResponseDTO(
        Long id,
        String login,
        String senha,
        Telefone telefone,
        Endereco endereco,
        PessoaFisica pessoa,
        String nomeImagem) {

    // public UsuarioResponseDTO(Usuario usuario) {
    //     this(usuario.getId(), usuario.getLoginUsu(), usuario.getSenha(), usuario.getTelefone(),
    //             usuario.getEndereco(), usuario.getPessoaFisica(), usuario.getNomeImagem());
    // }

    public static UsuarioResponseDTO valueOf(Usuario u) {
        if (u.getPessoaFisica() == null) 
            return new UsuarioResponseDTO(u.getId(),u.getLoginUsu(), u.getSenha(), null, null, null, null);
        
        return new UsuarioResponseDTO(u.getId(), 
            u.getLoginUsu(),
            u.getSenha(),
            u.getTelefone(),
            u.getEndereco(),
            u.getPessoaFisica(),
            u.getNomeImagem());
    }

}
