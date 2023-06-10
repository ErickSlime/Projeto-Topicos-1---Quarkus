package br.unitins.topicos1.dto;

public record DadosPessoaisResponseDTO(
    String login,
    String nome,
    String email,
    String cpf,
    String sexo
) {
    
    public DadosPessoaisResponseDTO (UsuarioResponseDTO usuario) {

        this(usuario.login(),
            usuario.pessoa().getNome(),
            usuario.pessoa().getEmail(),
            usuario.pessoa().getCpf(),
            usuario.pessoa().getSexo().getLabel());
    }
}
