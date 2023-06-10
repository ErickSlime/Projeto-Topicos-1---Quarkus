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
import br.unitins.topicos1.dto.EstadoDTO;
import br.unitins.topicos1.dto.EstadoResponseDTO;
import br.unitins.topicos1.service.EstadoService;

@Path("/estados")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EstadoResource {
    
    @Inject
    EstadoService estadoService;

    private static final Logger LOG = Logger.getLogger(CompraResource.class);

    @GET
    @RolesAllowed({"Admin","User"})
    public List<EstadoResponseDTO> getAll() {
        LOG.infof("Buscando todos Estados ");
        return estadoService.getAll();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"Admin","User"})
    public EstadoResponseDTO findById(@PathParam("id") Long id) {
        LOG.infof("Buscando estado especifico ");
        return estadoService.findById(id);
    }

    @POST
    @RolesAllowed({"Admin"})
    public Response insert(EstadoDTO dto) {
        try {
            EstadoResponseDTO estado = estadoService.create(dto);
            LOG.infof("Estado inserido com sucesso."+dto.nome());
            return Response.status(Status.CREATED).entity(estado).build();
        } catch(ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            LOG.errorf("Erro ao inserir estado: "+dto.nome());
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"Admin"})
    public Response update(@PathParam("id") Long id, EstadoDTO dto) {
        try {
            EstadoResponseDTO estado = estadoService.update(id, dto);
            LOG.infof("Estado atualizado com sucesso");
            return Response.ok(estado).status(Status.NO_CONTENT).build();
        } catch(ConstraintViolationException e) {
            LOG.errorf("Erro ao atualizar estado");
            Result result = new Result(e.getConstraintViolations());
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }      
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"Admin"})
    public Response delete(@PathParam("id") Long id) {
        estadoService.delete(id);
        LOG.infof("Estado deletado com sucesso");
        return Response.status(Status.NO_CONTENT).build();
    }


    @GET
    @Path("/count")
    @RolesAllowed({"Admin","User"})
    public long count(){
        LOG.infof("Contando todos Estados");
        return estadoService.count();
    }

    @GET
    @Path("/search/{nome}")
    @RolesAllowed({"Admin","User"})
    public List<EstadoResponseDTO> search(@PathParam("nome") String nome){
        LOG.infof("Procurando Estado");
        return estadoService.findByNome(nome);
        
    }
}

