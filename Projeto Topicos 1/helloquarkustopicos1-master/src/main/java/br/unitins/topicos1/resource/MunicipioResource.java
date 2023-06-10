package br.unitins.topicos1.resource;

import java.util.List;

import org.jboss.logging.Logger;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
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
import br.unitins.topicos1.dto.MunicipioDTO;
import br.unitins.topicos1.dto.MunicipioResponseDTO;
import br.unitins.topicos1.service.MunicipioService;

@Path("/municipios")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MunicipioResource {

    @Inject
    MunicipioService municipioService;

    private static final Logger LOG = Logger.getLogger(MunicipioResource.class);

    @GET
    @RolesAllowed({"Admin"})
    public List<MunicipioResponseDTO> getAll() {
        LOG.info("Buscando todos municipios.");
        LOG.debug("ERRO DE DEBUG.");
        return municipioService.getAll();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"Admin","User"})
    public MunicipioResponseDTO findById(@PathParam("id") Long id) {
        return municipioService.findById(id);
    }

    @POST
    @RolesAllowed({"Admin"})
    public Response insert(MunicipioDTO dto) {
        LOG.info("Inserindo um municipio." + dto.nome());
        LOG.infof("Inserindo um municipio: %s", dto.nome());
        try {
            MunicipioResponseDTO municipio = municipioService.create(dto);
            LOG.info("Municipio Criado com Sucesso.");
            return Response.status(Status.CREATED).entity(municipio).build();
        } catch (ConstraintViolationException e) {
            LOG.error("Erro ao inserir um municipio.");
            LOG.debug(e.getMessage());
            Result result = new Result(e.getConstraintViolations());
            return Response.status(Status.NOT_FOUND).entity(result).build();
        } catch (Exception e) {
            LOG.fatal("MEGUMIN NÃO É REAL");
            Result result = new Result(e.getMessage());
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }

    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"Admin"})
    public Response update(@PathParam("id") Long id, MunicipioDTO dto) {
        try {
            MunicipioResponseDTO municipio = municipioService.update(id, dto);
            LOG.infof("Municipio atualizado com sucesso");
            return Response.ok(municipio).status(Status.NO_CONTENT).build();
        } catch (ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            LOG.errorf("Erro ao atualizar municipio");
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"Admin"})
    public Response delete(@PathParam("id") Long id) {
        municipioService.delete(id);
        LOG.infof("Deletando municipio");
        return Response.status(Status.NO_CONTENT).build();
    }

    @GET
    @Path("/count")
    @RolesAllowed({"Admin","User"})
    public long count() {
        LOG.infof("Buscando quantidade de municipios registrados");
        return municipioService.count();
    }

    @GET
    @Path("/search/{nome}")
    @RolesAllowed({"Admin","User"})
    public List<MunicipioResponseDTO> search(@PathParam("nome") String nome) {
        LOG.infof("Buscando municipios pelo nome");
        return municipioService.findByNome(nome);

    }
}
