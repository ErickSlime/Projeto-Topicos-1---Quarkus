package br.unitins.topicos1.service;

import java.util.List;

import br.unitins.topicos1.dto.CadastroBasicoDTO;
import br.unitins.topicos1.dto.CompletarCadastroDTO;
import br.unitins.topicos1.dto.DadosPessoaisDTO;
import br.unitins.topicos1.dto.EnderecoDTO;
import br.unitins.topicos1.dto.ListaDesejDTO;
import br.unitins.topicos1.dto.ListaDesejoResponseDTO;
import br.unitins.topicos1.dto.SenhaDTO;
import br.unitins.topicos1.dto.TelefoneDTO;
import br.unitins.topicos1.dto.UsuarioDTO;
import br.unitins.topicos1.dto.UsuarioResponseDTO;
import br.unitins.topicos1.model.Usuario;

public interface UsuarioService {

    // recursos basicos
    List<UsuarioResponseDTO> getAll();

    UsuarioResponseDTO findById(Long id);

    UsuarioResponseDTO create(UsuarioDTO usuarioDTO);

    UsuarioResponseDTO update(Long id, UsuarioDTO usuarioDTO);

    void delete(Long id);

    Usuario findByLoginAndSenha(String login, String senha);

    public Usuario findByIdUsu(Long id);

    // recursos extras

    void insertListaDesejo(ListaDesejDTO listaDto);

    ListaDesejoResponseDTO getListaDesejo(Long id);

    List<UsuarioResponseDTO> findByNome(String nome);

    UsuarioResponseDTO findByLogin(String nome);

    public UsuarioResponseDTO update(Long id, String nomeImagem);

    long count();

    public UsuarioResponseDTO create(CadastroBasicoDTO cadastro);

    public UsuarioResponseDTO update(Long id, CompletarCadastroDTO usuarioDTO);

    // Usuario getByLoginAndSenha(String login, String senha);

    Usuario getByLogin(String login);

    void update (Long id, DadosPessoaisDTO dadosPessoaisDTO);

    void update (Long id, SenhaDTO senhaDTO);

    void updateTelefonePrincipal(Long id, TelefoneDTO telefonePrincipalDTO);

    public void updateEndereco(Long id, EnderecoDTO enderecoDTO);

}
