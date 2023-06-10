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
import br.unitins.topicos1.dto.PessoaFisicaDTO;
import br.unitins.topicos1.dto.PessoaFisicaResponseDTO;
import br.unitins.topicos1.service.PessoaFisicaService;

@Path("/pessoasfisicas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PessoaFisicaResource {

    @Inject
    PessoaFisicaService pessoaFisicaService;

    private static final Logger LOG = Logger.getLogger(MunicipioResource.class);

    @GET
    @RolesAllowed({"Admin"})
    public List<PessoaFisicaResponseDTO> getAll() {
        LOG.infof("Buscando todas Pessoas Fisicas registradas");
        return pessoaFisicaService.getAll();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"Admin"})
    public PessoaFisicaResponseDTO findById(@PathParam("id") Long id) {
        LOG.infof("Buscando Pessoa Fisica pelo id");
        return pessoaFisicaService.findById(id);
    }

    @POST
    @RolesAllowed({"Admin"})
    public Response insert(PessoaFisicaDTO dto) {
        try {
            PessoaFisicaResponseDTO pessoaFisica = pessoaFisicaService.create(dto);
            LOG.infof("Pessoa fisica criada com sucesso");
            return Response.status(Status.CREATED).entity(pessoaFisica).build();
        } catch (ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            LOG.errorf("Erro ao criar pessoa fisica");
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"Admin"})
    public Response update(@PathParam("id") Long id, PessoaFisicaDTO dto) {
        try {
            PessoaFisicaResponseDTO pessoaFisica = pessoaFisicaService.update(id, dto);
            pessoaFisicaService.update(id, dto);
            LOG.infof("Sucesso ao atualizar os dados de pessoa fisica");
            return Response.ok(pessoaFisica).status(Status.NO_CONTENT).build();
        } catch (ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            LOG.errorf("Erro ao atualizar os dados de pessoa fisica");
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"Admin"})
    public Response delete(@PathParam("id") Long id) {
        pessoaFisicaService.delete(id);
        LOG.infof("Pessoa Fisica deletada com sucesso");
        return Response.status(Status.NO_CONTENT).build();
    }

    @GET
    @Path("/count")
    @RolesAllowed({"Admin"})
    public long count() {
        LOG.infof("Buscando quantidade d pessoas fisicas registradas");
        return pessoaFisicaService.count();
    }

    @GET
    @Path("/search/{email}")
    @RolesAllowed({"Admin"})
    public List<PessoaFisicaResponseDTO> search(@PathParam("email") String email) {
        LOG.infof("Buscando pessoa fisica pelo email");
        return pessoaFisicaService.findByNome(email);

    }
}
