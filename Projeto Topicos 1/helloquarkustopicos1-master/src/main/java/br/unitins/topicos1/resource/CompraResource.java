package br.unitins.topicos1.resource;

import java.util.List;
import org.jboss.logging.Logger;
import org.eclipse.microprofile.jwt.JsonWebToken;

import br.unitins.topicos1.application.Result;
import br.unitins.topicos1.dto.CartaoCreditoDTO;
import br.unitins.topicos1.dto.CompraResponseDTO;
import br.unitins.topicos1.dto.ItemCompraDTO2;
import br.unitins.topicos1.dto.UsuarioResponseDTO;
import br.unitins.topicos1.service.CompraService;
import br.unitins.topicos1.service.ItemCompraService;
import br.unitins.topicos1.service.UsuarioServiceImpl;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/compras")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CompraResource {

    @Inject
    CompraService compraService;

    @Inject
    ItemCompraService itemCompraSevice;

    @Inject
    JsonWebToken jwt;

    @Inject
    UsuarioServiceImpl usuarioService;

    private static final Logger LOG = Logger.getLogger(CompraResource.class);

    @GET
    @RolesAllowed({"Admin"})
    public List<CompraResponseDTO> getAll() {
        LOG.infof("Buscando todas as compras realizadas ");
        return compraService.getAll();
    }

    @GET
    @RolesAllowed({"Admin","User"})
    @Path("/historicodecompras")
    public List<CompraResponseDTO> getAllComprasUsuario() {
        LOG.infof("Buscando todas as compras realizadas pelo usuario");
        String login = jwt.getSubject();

        UsuarioResponseDTO usuario = usuarioService.findByLogin(login);
        return compraService.getAllUsuario(usuario.id());
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"Admin","User"})
    public CompraResponseDTO findById(@PathParam("id") Long id) {
        LOG.infof("Buscando compra especifica");
        return compraService.findById(id);
    }

    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Admin","User"})
    @Path("/pagamentopix")
    public Response comprarItensPix() {
        try{
        String login = jwt.getSubject();

        UsuarioResponseDTO usuario = usuarioService.findByLogin(login);

        compraService.comprarItensPix(usuario.id());

        return Response.status(Status.CREATED).build();
        }catch(Exception e){
            LOG.error("Erro ao efetuar o pagamento com pix.", e);
            Result result = new Result(e.getMessage(), false);

            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Admin","User"})
    @Path("/pagamentocartao")
    public Response comprarItensCartaoCredito(CartaoCreditoDTO cartaoCreditoDTO) {
        try{
        String login = jwt.getSubject();

        UsuarioResponseDTO usuario = usuarioService.findByLogin(login);

        compraService.comprarItensCartaoCredito(usuario.id(),cartaoCreditoDTO);

        return Response.status(Status.CREATED).build();
    }catch(Exception e){
        LOG.error("Erro ao efetuar o pagamento com cartao.", e);
        Result result = new Result(e.getMessage(), false);

        return Response.status(Status.NOT_FOUND).entity(result).build();
    }
        
    }

    @POST
    @Path("/adicionaritemcarrinho")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Admin","User"})
    public Response adicionandoItem(ItemCompraDTO2 itemDTO) {
        LOG.infof("Adicionando item ao carrinho");
        try{
        String login = jwt.getSubject();
        UsuarioResponseDTO usuario = usuarioService.findByLogin(login);
        itemCompraSevice.create2(usuario.id(), itemDTO);

        return Response.status(Status.CREATED).build();
        }catch(ConstraintViolationException e){
            Result result = new Result(e.getConstraintViolations());
            LOG.errorf("Erro ao adicionar produto no carrinho");
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }catch(NullPointerException e){
            Result result = new Result(e.getMessage(), false);
            LOG.errorf("Erro ao adicionar produto no carrinho");
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @DELETE
    @Path("/deletaritemcarrinho/{idItemCarrinho}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Admin","User"})
    public Response deletarItem(@PathParam("idItemCarrinho") Long idItemCarrinho) {
        LOG.infof("deletando item do carrinho");
        String login = jwt.getSubject();

        UsuarioResponseDTO usuario = usuarioService.findByLogin(login);

        itemCompraSevice.delete(usuario.id(), idItemCarrinho);

        return Response.status(Status.CREATED).build();
    }


}