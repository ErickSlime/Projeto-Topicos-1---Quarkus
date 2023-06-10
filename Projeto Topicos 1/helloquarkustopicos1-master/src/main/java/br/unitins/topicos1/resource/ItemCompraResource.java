package br.unitins.topicos1.resource;

import java.util.List;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import br.unitins.topicos1.dto.ItemCompraResponseDTO;
import br.unitins.topicos1.dto.UsuarioResponseDTO;
import br.unitins.topicos1.service.ItemCompraService;
import br.unitins.topicos1.service.UsuarioServiceImpl;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/itemCompra")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ItemCompraResource {

    @Inject
    ItemCompraService itemCompraSevice;

    @Inject
    JsonWebToken jwt;

    @Inject
    UsuarioServiceImpl usuarioService;

    private static final Logger LOG = Logger.getLogger(CompraResource.class);
    
    @GET
    @RolesAllowed({"Admin"})
    public List<ItemCompraResponseDTO> getAll() {
        LOG.infof("Buscando todos Itens adicionados nos carrinhos ");
        return itemCompraSevice.getAll();
    }

    @GET
    @Path("/itensnocarrinho")
    @RolesAllowed({"Admin","User"})
    public List<ItemCompraResponseDTO> getAllCarrinho() {
        LOG.infof("Buscando itens do carrinho do usuario ");
        String login = jwt.getSubject();

        UsuarioResponseDTO usuario = usuarioService.findByLogin(login);
        return itemCompraSevice.getAllCarrinho(usuario.id());
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"Admin"})
    public ItemCompraResponseDTO findById(@PathParam("id") Long id) {
        LOG.infof("Buscando itens especifico adicionado nos carrinhos ");
        return itemCompraSevice.findById(id);
    }

    @GET
    @Path("/quantitenscarrinho")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Admin","User"})
    public long count() {
        LOG.infof("Buscando quantidade de itens no carrinho ");

        String login = jwt.getSubject();

        UsuarioResponseDTO usuario = usuarioService.findByLogin(login);

        return itemCompraSevice.countItemCompra(usuario.id());
    }

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Admin"})
    public long count2(){
        LOG.infof("Buscando quantidade de itens dos carrinhos ");
        return itemCompraSevice.count();
    }

}
