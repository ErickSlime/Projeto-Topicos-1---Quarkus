package br.unitins.topicos1.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import br.unitins.topicos1.dto.CadastroBasicoDTO;
import br.unitins.topicos1.dto.CompletarCadastroDTO;
import br.unitins.topicos1.dto.DadosPessoaisDTO;
import br.unitins.topicos1.dto.EnderecoDTO;
import br.unitins.topicos1.dto.ListaDesejDTO;
import br.unitins.topicos1.dto.ListaDesejoResponseDTO;
import br.unitins.topicos1.dto.PessoaFisicaDTO;
import br.unitins.topicos1.dto.SenhaDTO;
import br.unitins.topicos1.dto.TelefoneDTO;
import br.unitins.topicos1.dto.UsuarioDTO;
import br.unitins.topicos1.dto.UsuarioResponseDTO;
import br.unitins.topicos1.model.Bebida;
import br.unitins.topicos1.model.Endereco;
import br.unitins.topicos1.model.Perfil;
import br.unitins.topicos1.model.PessoaFisica;
import br.unitins.topicos1.model.Sexo;
import br.unitins.topicos1.model.Telefone;
import br.unitins.topicos1.model.Usuario;
import br.unitins.topicos1.repository.BebidaRepository;
import br.unitins.topicos1.repository.EnderecoRepository;
import br.unitins.topicos1.repository.MunicipioRepository;
import br.unitins.topicos1.repository.PessoaFisicaRepository;
import br.unitins.topicos1.repository.TelefoneRepository;
import br.unitins.topicos1.repository.UsuarioRepository;

@ApplicationScoped
public class UsuarioServiceImpl implements UsuarioService {

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    BebidaRepository bebidaRepository;

    @Inject
    MunicipioRepository municipioRepository;

    @Inject
    HashService hashService;

    @Inject
    EnderecoRepository enderecoRepository;

    @Inject
    TelefoneRepository telefoneRepository;

    @Inject
    PessoaFisicaRepository pessoaFisicaRepository;

    @Inject
    PessoaFisicaService pessoaFisicaService;

    @Inject
    Validator validator;

    @Override
    public List<UsuarioResponseDTO> getAll() {
        List<Usuario> list = usuarioRepository.listAll();
        return list.stream().map(u -> UsuarioResponseDTO.valueOf(u)).collect(Collectors.toList());
    }

    @Override
    public UsuarioResponseDTO findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id);
        if (usuario == null)
            throw new NotFoundException("Usuario não encontrada.");
        return UsuarioResponseDTO.valueOf(usuario);
    }

    @Override
    public Usuario findByIdUsu(Long id) {
        Usuario usuario = usuarioRepository.findById(id);
        if (usuario == null)
            throw new NotFoundException("Usuario não encontrada.");
        return usuario;
    }

    @Override
    @Transactional
    public UsuarioResponseDTO create(UsuarioDTO usuarioDTO) throws ConstraintViolationException {
        validar(usuarioDTO);

        Usuario entity = new Usuario();
        entity.setLoginUsu(usuarioDTO.login());
        entity.setSenha(hashService.getHashSenha(usuarioDTO.senha()));
        entity.setPessoaFisica(insertPessoaFisica(usuarioDTO.pessoa()));
        entity.setTelefone(createTelefone(usuarioDTO.telefone()));
        entity.setEndereco(createEndereco(usuarioDTO.endereco()));

        usuarioRepository.persist(entity);

        return UsuarioResponseDTO.valueOf(entity);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO create(CadastroBasicoDTO cadastro) throws ConstraintViolationException {

        Usuario entity = new Usuario();
        PessoaFisica pessoa = new PessoaFisica();
        pessoa.setEmail(cadastro.email());
        pessoaFisicaRepository.persist(pessoa);
        // Set<Perfil> perfil = new HashSet<>();
        // perfil.add(Perfil.USER);
        entity.setLoginUsu(cadastro.login());
        entity.setSenha(hashService.getHashSenha(cadastro.senha()));
        entity.setPessoaFisica(pessoa);
        entity.addPerfis(Perfil.USER);

        usuarioRepository.persist(entity);

        return UsuarioResponseDTO.valueOf(entity);
    }

    private PessoaFisica insertPessoaFisica(PessoaFisicaDTO pessoaFisicaDTO) throws ConstraintViolationException {

        return pessoaFisicaService.createPessoaFisica(pessoaFisicaDTO);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO update(Long id, UsuarioDTO usuarioDTO) throws ConstraintViolationException {
        validar(usuarioDTO);

        Usuario entity = usuarioRepository.findById(id);
        entity.setLoginUsu(usuarioDTO.login());
        entity.setSenha(hashService.getHashSenha(usuarioDTO.senha()));
        entity.setTelefone(createTelefone(usuarioDTO.telefone()));
        entity.setEndereco(createEndereco(usuarioDTO.endereco()));
        entity.setPessoaFisica(createPessoaFisica(usuarioDTO.pessoa()));

        return UsuarioResponseDTO.valueOf(entity);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO update(Long id, CompletarCadastroDTO cadastro) throws ConstraintViolationException {

        Usuario entity = usuarioRepository.findById(id);
        entity.setEndereco(createEndereco(cadastro.endereco()));
        entity.setPessoaFisica(createPessoaFisica(cadastro.pessoa()));
        entity.setTelefone(createTelefone(cadastro.telefone()));

        return UsuarioResponseDTO.valueOf(entity);
    }

    private void validar(UsuarioDTO usuarioDTO) throws ConstraintViolationException {
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(usuarioDTO);
        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);

    }

    private void validarTel(TelefoneDTO telefoneDTO) throws ConstraintViolationException {
        Set<ConstraintViolation<TelefoneDTO>> violations = validator.validate(telefoneDTO);
        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);

    }

    private void validarPess(PessoaFisicaDTO pessoaDTO) throws ConstraintViolationException {
        Set<ConstraintViolation<PessoaFisicaDTO>> violations = validator.validate(pessoaDTO);
        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);

    }

    private void validarEnd(EnderecoDTO enderecoDTO) throws ConstraintViolationException {
        Set<ConstraintViolation<EnderecoDTO>> violations = validator.validate(enderecoDTO);
        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);

    }

    @Override
    @Transactional
    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public List<UsuarioResponseDTO> findByNome(String nome) {
        List<Usuario> list = usuarioRepository.findByNome(nome);
        return list.stream().map(u -> UsuarioResponseDTO.valueOf(u)).collect(Collectors.toList());
    }

    @Override
    public UsuarioResponseDTO findByLogin(String login) {
        Usuario usuario = usuarioRepository.findByLogin(login);
        if (usuario == null)
            throw new NotFoundException("Usuário não encontrado.");
        return UsuarioResponseDTO.valueOf(usuario);
    }

    @Override
    public long count() {
        return usuarioRepository.count();
    }

    public ListaDesejoResponseDTO getListaDesejo(Long id) throws NullPointerException {

        Usuario usuario = usuarioRepository.findById(id);

        if (usuario == null)
            throw new NullPointerException("usuario não encontrado");

        return new ListaDesejoResponseDTO(usuario);
    }

    public void insertListaDesejo(ListaDesejDTO listaDto) throws NullPointerException {

        validarListDe(listaDto);

        Bebida bebida = bebidaRepository.findById(listaDto.idProduto());

        Usuario usuario = usuarioRepository.findById(listaDto.idUsuario());

        if (usuario == null)
            throw new NullPointerException("usuario não encontrado");

            for(int i=0; i<usuario.getBebidas().size();i++){
                if(usuario.getBebidas().get(i).equals(bebida)) throw new NullPointerException("Bebida ja adicionada");
            }  

        usuario.setBebida(bebidaRepository.findById(listaDto.idProduto()));
    }

    private void validarListDe(ListaDesejDTO listaDesejoDto) throws ConstraintViolationException {

        Set<ConstraintViolation<ListaDesejDTO>> violations = validator.validate(listaDesejoDto);

        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);

    }

    private Telefone createTelefone(TelefoneDTO telefone2) throws ConstraintViolationException {
        validarTel(telefone2);

        Telefone telefone = new Telefone();

        telefone.setCodigoArea(telefone2.codigoArea());
        telefone.setNumero(telefone2.numero());

        telefoneRepository.persist(telefone);

        return telefone;
    }

    private PessoaFisica createPessoaFisica(PessoaFisicaDTO pessoa) throws ConstraintViolationException {
        validarPess(pessoa);

        PessoaFisica pessoaf = new PessoaFisica();

        pessoaf.setNome(pessoa.nome());
        pessoaf.setEmail(pessoa.email());
        pessoaf.setCpf(pessoa.cpf());
        pessoaf.setSexo(Sexo.valueOf(pessoa.sexo()));

        pessoaFisicaRepository.persist(pessoaf);

        return pessoaf;
    }

    private Endereco createEndereco(EnderecoDTO enderecoDto) throws ConstraintViolationException {

        validarEnd(enderecoDto);

        Endereco endereco = new Endereco();

        endereco.setLogradouro(enderecoDto.logradouro());

        endereco.setBairro(enderecoDto.bairro());

        endereco.setCep(enderecoDto.cep());

        endereco.setNumero(enderecoDto.numero());

        endereco.setComplemento(enderecoDto.complemento());

        endereco.setMunicipio(municipioRepository.findById(enderecoDto.idMunicipio()));

        enderecoRepository.persist(endereco);

        return endereco;
    }

    @Override
    public Usuario findByLoginAndSenha(String login, String senha) {
        Usuario usuario = usuarioRepository.findByLoginAndSenha(login, senha);
         return usuario;
    }

    @Override
    public Usuario getByLogin(String login) {

        Usuario usuario = usuarioRepository.findByLogin(login);

        if (usuario == null) {
            throw new NullPointerException("usuario não encontrado");
        }
        return usuario;
    }

    // @Override
    // public Usuario getByLoginAndSenha(String login, String senha) {

    //     Usuario usuario = usuarioRepository.findByLoginAndSenha(login, senha);

    //     return usuario;
    // }

    @Override
    @Transactional
    public void update(Long id, DadosPessoaisDTO dadosPessoaisDTO) {

        Usuario entity = usuarioRepository.findById(id);

        entity.getPessoaFisica().setEmail(dadosPessoaisDTO.email());

        entity.getPessoaFisica().setSexo(Sexo.valueOf(dadosPessoaisDTO.sexo()));
    }

    @Override
    public void update(Long id, SenhaDTO senhaDTO) {

        Usuario entity = usuarioRepository.findById(id);

        if (entity.getSenha().equals(hashService.getHashSenha(senhaDTO.senhaAntiga())))
            entity.setSenha(hashService.getHashSenha(senhaDTO.senhaNova()));

        else
            throw new NotAuthorizedException("A senha inserida não corresponde à senha atual, acesso negado");
    }

    @Override
    @Transactional
    public void updateTelefonePrincipal(Long id, TelefoneDTO telefonePrincipalDTO) {

        Usuario entity = usuarioRepository.findById(id);

        Long idTelefone = entity.getTelefone().getId();

        entity.setTelefone(createTelefone(telefonePrincipalDTO));

        deleteTelefone(idTelefone);
    }

    private void deleteTelefone(Long id) throws NotFoundException, IllegalArgumentException {

        if (id == null)
            throw new IllegalArgumentException("Número inválido");

        Telefone telefone = telefoneRepository.findById(id);

        if (telefoneRepository.isPersistent(telefone))
            telefoneRepository.delete(telefone);

        else
            throw new NotFoundException("Nenhum Telefone encontrado");
    }

    @Override
    @Transactional
    public void updateEndereco(Long id, EnderecoDTO enderecoDTO) {

        Usuario entity = usuarioRepository.findById(id);
        Long idEndereco = entity.getEndereco().getId();

        entity.setEndereco(createEndereco(enderecoDTO));
        deleteEndereco(idEndereco);

    }

    private void deleteEndereco(Long id) throws NotFoundException, IllegalArgumentException {

        if (id == null)
            throw new IllegalArgumentException("Número inválido");

        Endereco endereco = enderecoRepository.findById(id);

        if (enderecoRepository.isPersistent(endereco))
            enderecoRepository.delete(endereco);

        else
            throw new NotFoundException("Nenhum Endereco encontrado");
    }

    @Override
    @Transactional
    public UsuarioResponseDTO update(Long id, String nomeImagem) {
   
        Usuario entity = usuarioRepository.findById(id);
        entity.setNomeImagem(nomeImagem);

        return UsuarioResponseDTO.valueOf(entity);
    }

}
