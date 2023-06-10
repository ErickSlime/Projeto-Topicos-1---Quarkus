package br.unitins.topicos1.resource;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import br.unitins.topicos1.application.Result;
import br.unitins.topicos1.dto.AuthUsuarioDTO;
import br.unitins.topicos1.dto.CadastroBasicoDTO;
import br.unitins.topicos1.dto.UsuarioResponseDTO;
import br.unitins.topicos1.model.Usuario;
import br.unitins.topicos1.service.HashService;
import br.unitins.topicos1.service.TokenJwtService;
import br.unitins.topicos1.service.UsuarioService;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/auth")
public class AuthResource {

    @Inject
    HashService hashService;

    @Inject
    UsuarioService usuarioService;

    @Inject
    TokenJwtService tokenService;

    @Inject
    JsonWebToken jwt;

    private static final Logger LOG = Logger.getLogger(AuthResource.class);

    @POST
    @Path("/login")
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(AuthUsuarioDTO authDTO) {
       try{ 
        String hash = hashService.getHashSenha(authDTO.senha());

        Usuario usuario = usuarioService.findByLoginAndSenha(authDTO.login(), hash);

        if (usuario == null) {
            return Response.status(Status.NO_CONTENT)
                            .entity("Usuario não encontrado").build();
        }

        LOG.info("Login do usuário bem-sucedido: " + authDTO.login());

        return Response.ok()
                    .header("Authorization", tokenService.generateJwt(usuario))
                    .build();
    }catch(Exception e){
        LOG.error("Erro durante o login do usuário: " + authDTO.login(), e);
            throw e;
    }
}

    @POST
    @Path("/cadastro")
    @Produces(MediaType.TEXT_PLAIN)
    public Response cadastro(CadastroBasicoDTO cadastro) {
        try {
            UsuarioResponseDTO Usuario = usuarioService.create(cadastro);
            LOG.info("Cadastro Realizado:  " + cadastro.login());
            return Response.status(Status.CREATED).entity(Usuario).build();
        } catch (ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            LOG.error("Erro durante o cadastro do usuário: " + cadastro.login());
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    
}
