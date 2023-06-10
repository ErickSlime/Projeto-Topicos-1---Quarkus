package br.unitins;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.Test;

import br.unitins.topicos1.dto.ItemCompraDTO2;
import br.unitins.topicos1.model.Usuario;
import br.unitins.topicos1.repository.CompraRepository;
import br.unitins.topicos1.service.TokenJwtService;
import br.unitins.topicos1.service.UsuarioService;

import static io.restassured.RestAssured.given;

import jakarta.inject.Inject;


@QuarkusTest
public class CompraResourceTest{
    
    @Inject
    TokenJwtService jwt;

    @Inject
    UsuarioService usuarioService;

    @Inject
    CompraRepository compraRepository;

    @Test
    @TestSecurity(user = "testUser", roles = {"Admin","User"})
    void testGetAll() {
        given()
        .when().get("/compras")
        .then()
        .statusCode(200);
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"Admin","User"})
    public void testFindById() {
        Long id = 1l;
        given()
                .when().get("/compras/" + id)
                .then()
                .statusCode(200);
    }

        @Test
    public void testInsertPagamento() {
        Usuario usuario = usuarioService.getByLogin("Joao");
        String token = jwt.generateJwt(usuario);
     
        given().header("Authorization", "Bearer " + token)
        .when().patch("/compras/pagamentopix")
        .then()
        .statusCode(201);

    }

    @Test
public void testInsertItemCarrinho() {

    Usuario usuario = usuarioService.findByIdUsu(1l);
    String token = jwt.generateJwt(usuario);

    ItemCompraDTO2 item = new ItemCompraDTO2(2, 4l);

given().header("Authorization", "Bearer " + token)
.contentType(ContentType.JSON)
.body(item)
.when().post("/compras/adicionaritemcarrinho")
.then()
.statusCode(201);
}

@Test
public void testDeleteItemCarrinho() {
Usuario usuario = usuarioService.getByLogin("Erick");
    String token = jwt.generateJwt(usuario);

    ItemCompraDTO2 item = new ItemCompraDTO2(2, 4l);

given().header("Authorization", "Bearer " + token)
.contentType(ContentType.JSON)
.body(item)
.when().post("/compras/adicionaritemcarrinho")
.then()
.statusCode(201);

given().header("Authorization", "Bearer " + token)
.when().delete("/compras/deletaritemcarrinho/" + 3l)
.then()
.statusCode(201);

}
}
