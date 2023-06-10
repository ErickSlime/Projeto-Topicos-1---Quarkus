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
import br.unitins.topicos1.dto.MarcaDTO;
import br.unitins.topicos1.dto.MarcaResponseDTO;
import br.unitins.topicos1.service.MarcaService;

@Path("/marcas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MarcaResource {
    
    @Inject
    MarcaService marcaService;

    private static final Logger LOG = Logger.getLogger(CompraResource.class);

    @GET
    @RolesAllowed({"Admin","User"})
    public List<MarcaResponseDTO> getAll() {
        LOG.infof("Buscando todas as marcas ");
        return marcaService.getAll();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"Admin","User"})
    public MarcaResponseDTO findById(@PathParam("id") Long id) {
        LOG.infof("Buscando marca especifica pelo id");
        return marcaService.findById(id);
    }

    @POST
    @RolesAllowed({"Admin"})
    public Response insert(MarcaDTO dto) {
        try {
            MarcaResponseDTO marca = marcaService.create(dto);
            LOG.infof("Marca adicionada com sucesso");
            return Response.status(Status.CREATED).entity(marca).build();
        } catch(ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            LOG.errorf("Erro ao adicionar Marca ");
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"Admin"})
    public Response update(@PathParam("id") Long id, MarcaDTO dto) {
        try {
            MarcaResponseDTO marca = marcaService.update(id, dto);
            LOG.infof("Sucesso ao atualizar marca");
            return Response.ok(marca).status(Status.NO_CONTENT).build();
        } catch(ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            LOG.errorf("Erro ao atualizar a marca "+dto.nome());
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }      
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"Admin"})
    public Response delete(@PathParam("id") Long id) {
        marcaService.delete(id);
        LOG.infof("Sucesso ao deletar marca");
        return Response.status(Status.NO_CONTENT).build();
    }


    @GET
    @Path("/count")
    @RolesAllowed({"Admin","User"})
    public long count(){
        LOG.infof("Buscando quantidade de marcas registradas");
        return marcaService.count();
    }

    @GET
    @Path("/search/{nome}")
    @RolesAllowed({"Admin","User"})
    public List<MarcaResponseDTO> search(@PathParam("nome") String nome){
        LOG.infof("Buscando marca pelo nome");
        return marcaService.findByNome(nome);
        
    }
}

