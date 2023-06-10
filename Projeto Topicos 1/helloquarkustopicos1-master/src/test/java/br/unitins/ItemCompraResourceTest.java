package br.unitins;



import br.unitins.topicos1.model.Usuario;
import br.unitins.topicos1.service.ItemCompraService;
import br.unitins.topicos1.service.TokenJwtService;
import br.unitins.topicos1.service.UsuarioServiceImpl;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;


import static io.restassured.RestAssured.given;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

@QuarkusTest
public class ItemCompraResourceTest {

    @Inject
    ItemCompraService itemCompraSevice;

    @Inject
    TokenJwtService jwt;

    @Inject
    UsuarioServiceImpl usuarioService;

    @Test
    @TestSecurity(user = "testUser", roles = {"Admin","User"})
    public void testGetAll() {
        given()
                .when().get("/itemCompra")
                .then()
                .statusCode(200);
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"Admin","User"})
    public void testFindById() {
        Long id = 1l;
        given()
                .when().get("/itemCompra/"+id)
                .then()
                .statusCode(200);
    }

    @Test
    public void testCountItensCarrinho() {
        Usuario usuario = usuarioService.findByIdUsu(2l);
        String token = jwt.generateJwt(usuario);

        given()
        .header("Authorization", "Bearer " + token)
    .when()
        .get("/itemCompra/quantitenscarrinho/")
    .then()
        .statusCode(200);
    }

    @Test
    public void testGetAllCarrinho() {
        Usuario usuario = usuarioService.findByIdUsu(2l);
        String token = jwt.generateJwt(usuario);

        given()
        .header("Authorization", "Bearer " + token)
    .when()
        .get("/itemCompra/itensnocarrinho/")
    .then()
        .statusCode(200);
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"Admin","User"})
    public void testCount2() {
        given()
                .when().get("/itemCompra/count/")
                .then()
                .statusCode(200);
    }

}
