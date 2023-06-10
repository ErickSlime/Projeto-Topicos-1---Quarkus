package br.unitins.topicos1.resource;

import java.io.IOException;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import br.unitins.topicos1.application.Result;
import br.unitins.topicos1.dto.CompletarCadastroDTO;
import br.unitins.topicos1.dto.DadosPessoaisDTO;
import br.unitins.topicos1.dto.DadosPessoaisResponseDTO;
import br.unitins.topicos1.dto.EnderecoDTO;
import br.unitins.topicos1.dto.ListaDesejDTO;
import br.unitins.topicos1.dto.ListaDesejoResponseDTO;
import br.unitins.topicos1.dto.SenhaDTO;
import br.unitins.topicos1.dto.TelefoneDTO;
import br.unitins.topicos1.dto.UsuarioResponseDTO;
import br.unitins.topicos1.form.ImagemForm;
import br.unitins.topicos1.service.FileService;
import br.unitins.topicos1.service.ItemCompraService;
import br.unitins.topicos1.service.UsuarioService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;

@Path("/usuariologado")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioLogadoResource {

    @Inject
    JsonWebToken jwt;

    @Inject
    UsuarioService usuarioService;
    
    @Inject
    ItemCompraService itemCompraSevice;

    @Inject
    FileService fileService;

    private static final Logger LOG = Logger.getLogger(MunicipioResource.class);

    @GET
    @Path("/dadospessoais")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Admin","User"})
    public Response getDadosPessoais() {
        LOG.infof("Buscando dados pessoais do usuario logado");
        String login = jwt.getSubject();

        DadosPessoaisResponseDTO dadosPessoaisUsuario = new DadosPessoaisResponseDTO(usuarioService.findByLogin(login));

        return Response.ok(dadosPessoaisUsuario).build();
    }

    @PATCH
    @Path("/atualizardadospessoais")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Admin","User"})
    public Response updateDadosPessoais(DadosPessoaisDTO dadosPessoaisDTO) {
        LOG.infof("Atualizando dados pessoais do usuario");
        String login = jwt.getSubject();

        UsuarioResponseDTO usuario = usuarioService.findByLogin(login);

        usuarioService.update(usuario.id(), dadosPessoaisDTO);

        return Response.status(Status.NO_CONTENT).build();
    }

    @PATCH
    @Path("/senhausuario")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Admin","User"})
    public Response updateSenha(SenhaDTO senhaDTO) {
        LOG.infof("Alterando senha do usuario");
        String login = jwt.getSubject();

        UsuarioResponseDTO usuario = usuarioService.findByLogin(login);

        usuarioService.update(usuario.id(), senhaDTO);

        return Response.status(Status.NO_CONTENT).build();
    }

    @PATCH
    @Path("/completarcadastro")
    @RolesAllowed({"User"})
    public Response update(CompletarCadastroDTO dto) {
        try {
            String login = jwt.getSubject();

            UsuarioResponseDTO usuario = usuarioService.findByLogin(login);
            usuarioService.update(usuario.id(), dto);
            LOG.infof("Cadastro completado com sucesso");
            return Response.ok(usuario).status(Status.NO_CONTENT).build();
        } catch (ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            LOG.errorf("Erro ao completar cadastro do usuario");
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @PATCH
    @Path("/telefoneusuario")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Admin","User"})
    public Response updateTelefonePrincipal(TelefoneDTO telefonePrincipalDTO) {
        LOG.infof("Alterando telefone do usuario");
        String login = jwt.getSubject();

        UsuarioResponseDTO usuario = usuarioService.findByLogin(login);

        usuarioService.updateTelefonePrincipal(usuario.id(), telefonePrincipalDTO);

        return Response.status(Status.NO_CONTENT).build();
    }

    @PATCH
    @Path("/enderecousuario")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Admin","User"})
    public Response updateEndereco(EnderecoDTO enderecoDTO) {
        LOG.infof("Atualizando Endereco do usuario");
        String login = jwt.getSubject();

        UsuarioResponseDTO usuario = usuarioService.findByLogin(login);

        usuarioService.updateEndereco(usuario.id(), enderecoDTO);

        return Response.status(Status.NO_CONTENT).build();
    }

    @PATCH
    @Path("/novaimagem")
    @RolesAllowed({"Admin","User"})
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response salvarImagem(@MultipartForm ImagemForm form){
        String nomeImagem = "";

        try {
            nomeImagem = fileService.salvarImagemUsuario(form.getImagem(), form.getNomeImagem());
            LOG.infof("Imagem salva");
        } catch (IOException e) {
            Result result = new Result(e.getMessage());
            LOG.errorf("Erro ao salvar imagem");
            return Response.status(Status.CONFLICT).entity(result).build();
        }
        String login = jwt.getSubject();
        UsuarioResponseDTO usuario = usuarioService.findByLogin(login);

        usuario = usuarioService.update(usuario.id(), nomeImagem);

        return Response.ok(usuario).build();

    }

    @GET
    @Path("/download/{nomeImagem}")
    @RolesAllowed({"Admin","User"})
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@PathParam("nomeImagem") String nomeImagem) {
        LOG.infof("Fazendo download da Imagem");
        ResponseBuilder response = Response.ok(fileService.download(nomeImagem));
        response.header("Content-Disposition", "attachment;filename="+nomeImagem);
        return response.build();
    }

    @GET
    @Path("/listadesejo")
    @RolesAllowed({"Admin","User"})
    public ListaDesejoResponseDTO getListaDesejo() {
        LOG.infof("Lista de desejo do Usuario");
        String login = jwt.getSubject();

        UsuarioResponseDTO usuario = usuarioService.findByLogin(login);

        return usuarioService.getListaDesejo(usuario.id());
    }

    @POST
    @Path("/listadesejo/{idProduto}")
    @Transactional
    @RolesAllowed({"Admin","User"})
    public Response insertListaDesejo(@PathParam("idProduto") Long idProduto) {

        try {
            String login = jwt.getSubject();

            UsuarioResponseDTO usuario = usuarioService.findByLogin(login);
            ListaDesejDTO listaDto = new ListaDesejDTO(usuario.id(), idProduto);
            usuarioService.insertListaDesejo(listaDto);
            LOG.infof("Produto inserido na lista de desejo");
            return Response.status(Status.CREATED).build();
        } catch (ConstraintViolationException e) {

            Result result = new Result(e.getConstraintViolations());
            LOG.errorf("Erro ao inserir produto na lista de desejo");
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }catch (NullPointerException e) {
            Result result = new Result(e.getMessage(), false);
            LOG.errorf("Erro ao inserir produto na lista de desejo");
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }
}
