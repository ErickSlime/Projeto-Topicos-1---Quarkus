package br.unitins.topicos1.resource;

import java.util.List;

import org.jboss.logging.Logger;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import br.unitins.topicos1.application.Result;
import br.unitins.topicos1.dto.ListaDesejDTO;
import br.unitins.topicos1.dto.ListaDesejoResponseDTO;
import br.unitins.topicos1.dto.UsuarioDTO;
import br.unitins.topicos1.dto.UsuarioResponseDTO;
import br.unitins.topicos1.service.UsuarioService;

@Path("/usuarios")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    UsuarioService usuarioService;

    private static final Logger LOG = Logger.getLogger(MunicipioResource.class);

    @GET
    @RolesAllowed({"Admin"})
    public List<UsuarioResponseDTO> getAll() {
        LOG.infof("Buscando todos Usuarios registrados");
        return usuarioService.getAll();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"Admin"})
    public UsuarioResponseDTO findById(@PathParam("id") Long id) {
        LOG.infof("Buscando Usuario pelo id");
        return usuarioService.findById(id);
    }

    @POST
    @RolesAllowed({"Admin"})
    public Response insert(UsuarioDTO dto) {
        try {
            UsuarioResponseDTO Usuario = usuarioService.create(dto);
            LOG.infof("Usuario criado com sucesso");
            return Response.status(Status.CREATED).entity(Usuario).build();
        } catch (ConstraintViolationException e) {
            LOG.errorf("Erro ao criar usuario");
            Result result = new Result(e.getConstraintViolations());
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }catch(IllegalArgumentException e){
            LOG.errorf("Erro ao criar usuario");
            Result result = new Result(e.getMessage(),false);
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"Admin"})
    public Response update(@PathParam("id") Long id, UsuarioDTO dto) {
        try {
            UsuarioResponseDTO usuario = usuarioService.update(id, dto);
            LOG.infof("Atualizando dados do Usuario");
            return Response.ok(usuario).status(Status.NO_CONTENT).build();
        } catch (ConstraintViolationException e) {
            LOG.errorf("Erro ao atualizar dados do Usuario");
            Result result = new Result(e.getConstraintViolations());
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"Admin"})
    public Response delete(@PathParam("id") Long id) {
        LOG.infof("Deletando dados de um usuario");
        usuarioService.delete(id);
        return Response.status(Status.NO_CONTENT).build();
    }

    @GET
    @Path("/count")
    @RolesAllowed({"Admin"})
    public long count() {
        LOG.infof("Buscando quantidade de Usuarios registrados");
        return usuarioService.count();
    }

    @GET
    @Path("/listadesejo/{idUsuario}")
    @RolesAllowed({"Admin"})
    public ListaDesejoResponseDTO getListaDesejo(@PathParam("idUsuario") Long idUsuario) {
        LOG.infof("Lista de desejo do Usuario especifico");

        return usuarioService.getListaDesejo(idUsuario);
    }

    @POST
    @Path("/listadesejo")
    @Transactional
    @RolesAllowed({"Admin"})
    public Response insertListaDesejo(ListaDesejDTO listaDto) {

        try {

            usuarioService.insertListaDesejo(listaDto);
            LOG.infof("Produto inserido na lista de desejo");
            return Response.status(Status.CREATED).build();
        } catch (ConstraintViolationException e) {

            Result result = new Result(e.getConstraintViolations());
            LOG.errorf("Erro ao inserir produto na lista de desejo");
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @GET
    @Path("/search/{loginUsu}")
    @RolesAllowed({"Admin"})
    public List<UsuarioResponseDTO> search(@PathParam("loginUsu") String loginUsu) {
        LOG.infof("Buscando Usuario pelo nome");
        return usuarioService.findByNome(loginUsu);

    }
}
