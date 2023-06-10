package br.unitins.topicos1.resource;

import java.io.IOException;
import java.util.List;

import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;

import br.unitins.topicos1.application.Result;
import br.unitins.topicos1.dto.BebidaDTO;
import br.unitins.topicos1.dto.BebidaResponseDTO;
import br.unitins.topicos1.form.ImagemForm;
import br.unitins.topicos1.service.BebidaService;
import br.unitins.topicos1.service.FileService;

@Path("/bebidas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BebidaResource {
    
    @Inject
    BebidaService bebidaService;

    @Inject
    FileService fileService;

    private static final Logger LOG = Logger.getLogger(BebidaResource.class);

    @GET
    @RolesAllowed({"Admin","User"})
    public List<BebidaResponseDTO> getAll() {
        LOG.info("Buscando todas os produtos");
        LOG.debug("ERRO DE DEBUG.");
        return bebidaService.getAll();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"Admin","User"})
    public BebidaResponseDTO findById(@PathParam("id") Long id) {
        LOG.infof("Buscando produtos por ID. ", id);
        return bebidaService.findById(id);
    }

    @POST
    @RolesAllowed({"Admin"})
    public Response insert(BebidaDTO dto) {
        try {
            BebidaResponseDTO bebida = bebidaService.create(dto);
            LOG.infof("Criando Produto");
            return Response.status(Status.CREATED).entity(bebida).build();
        } catch(ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            LOG.errorf("Erro ao criar Produto");
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }catch(Exception e) {
            Result result = new Result(e.getMessage(), false);
            LOG.errorf("Erro ao criar Produto");
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"Admin"})
    public Response update(@PathParam("id") Long id, BebidaDTO dto) {
        try {
            BebidaResponseDTO bebida = bebidaService.update(id, dto);
            bebidaService.update(id, dto);
            LOG.infof("Produto (%d) atualizado com sucesso.", id);
            return Response.ok(bebida).status(Status.NO_CONTENT).build();
        } catch(ConstraintViolationException e) {
            LOG.errorf("Erro ao atualizar um produto. ", id, e);
            Result result = new Result(e.getConstraintViolations());
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }catch(Exception e) {
            LOG.fatal("Erro sem identificacao: " + e.getMessage());
            Result result = new Result(e.getMessage(), false);
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }      
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"Admin"})
    public Response delete(@PathParam("id") Long id) {
        try{
        bebidaService.delete(id);
        LOG.infof("Produto excluído com sucesso.", id);
        return Response.status(Status.NO_CONTENT).build();
    } catch (IllegalArgumentException e) {
        LOG.error("Erro ao deletar produto: parâmetros inválidos.", e);
        throw e;
    }
}


    @GET
    @Path("/count")
    @RolesAllowed({"Admin","User"})
    public long count(){
        LOG.info("Contando todos os produtos.");
        return bebidaService.count();
    }

    @GET
    @Path("/search/{nome}")
    @RolesAllowed({"Admin","User"})
    public List<BebidaResponseDTO> search(@PathParam("nome") String nome){
        LOG.infof("Buscando produto pelo nome. ", nome);
        return bebidaService.findByNome(nome);
        
    }

    @GET
    @Path("/procuraportipobebida/{tipoBebida}")
    @RolesAllowed({"Admin","User"})
    public List<BebidaResponseDTO> getByTipoBebida(@PathParam("tipoBebida") Integer id) throws IndexOutOfBoundsException {
        LOG.infof("Buscando pelo Tipo da Bebida ", id);
        return bebidaService.getByTipoBebida(id);
    }

    @GET
    @Path("/prcurarpormarca/{marca}")
    @RolesAllowed({"Admin","User"})
    public List<BebidaResponseDTO> getByMarca (@PathParam("marca") String nomeMarca) {
        LOG.infof("Buscando pelo nome da marca. ", nomeMarca);
        return bebidaService.getByMarca(nomeMarca);
    }

    @PATCH
    @Path("/novaimagem/{id}")
    @RolesAllowed({"Admin","User"})
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response salvarImagem(@MultipartForm ImagemForm form, @PathParam("id") Long id){
        String nomeImagem = "";

        try {
            nomeImagem = fileService.salvarImagemBebida(form.getImagem(), form.getNomeImagem());
            LOG.infof("Imagem salva com sucesso: %s", nomeImagem);
        } catch (IOException e) {
            LOG.error("Erro ao salvar a imagem do produto.", e);
            Result result = new Result(e.getMessage());
            return Response.status(Status.CONFLICT).entity(result).build();
        }
        

        BebidaResponseDTO bebida = bebidaService.update(id, nomeImagem);

        return Response.ok(bebida).build();

    }

    @GET
    @Path("/download/{nomeImagem}")
    @RolesAllowed({"Admin","User"})
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@PathParam("nomeImagem") String nomeImagem) {
        try{
        LOG.infof("Download do arquivo %s concluído com sucesso.", nomeImagem);
        ResponseBuilder response = Response.ok(fileService.downloadBebida(nomeImagem));
        response.header("Content-Disposition", "attachment;filename="+nomeImagem);
        return response.build();
    } catch (Exception e) {
        LOG.errorf("Erro ao realizar o download do arquivo: %s", nomeImagem, e);
        return Response
                .status(Status.INTERNAL_SERVER_ERROR)
                .build();
    }
    }
}

