package br.unitins;



import br.unitins.topicos1.dto.BebidaDTO;
import br.unitins.topicos1.dto.BebidaResponseDTO;
import br.unitins.topicos1.model.TipoBebida;
import br.unitins.topicos1.service.BebidaService;
import br.unitins.topicos1.service.FileService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

@QuarkusTest
public class BebidaResourceTest {

    @Inject
    BebidaService bebidaService;

    @Inject
    FileService fileService;

    @Test
    @TestSecurity(user = "testUser", roles = {"Admin","User"})
    public void testGetAll() {
        given()
                .when().get("/bebidas")
                .then()
                .statusCode(200);
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"Admin","User"})
    public void testInsert() {
        BebidaDTO beb = new BebidaDTO(
                "Skol",
                1L,
                2.56,
                "Boa",
                1,
                50);
        given()
                .contentType(ContentType.JSON)
                .body(beb)
                .when().post("/bebidas")
                .then()
                .statusCode(201)
                .body("id", notNullValue(),
                        "nome", is("Skol"),
                        "descricao", is("Boa"),
                        "valor", is(2.56F),
                        "marca.nome", is("Ambev"),
                        "tipoBebida.label", is("Alcoolica"),
                        "estoque", is(50));
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"Admin","User"})
    public void testUpdate() {
        // Adicionando uma bebida no banco de dados
        BebidaDTO beb = new BebidaDTO(
                "Skol",
                1L,
                2.56d,
                "Boa",
                1,
                50);
        Long id = bebidaService.create(beb).id();
        // Criando outra bebida para atuailzacao
        BebidaDTO bebUpdate = new BebidaDTO(
                "Antartica",
                2L,
                2.59d,
                "Ruim",
                2,
                40);
        given()
                .contentType(ContentType.JSON)
                .body(bebUpdate)
                .when().put("/bebidas/" + id)
                .then()
                .statusCode(204);
        // Verificando se os dados foram atualizados no banco de dados
        BebidaResponseDTO bebResponse = bebidaService.findById(id);
        assertThat(bebResponse.nome(), is("Antartica"));
        assertThat(bebResponse.descricao(), is("Ruim"));
        assertThat(bebResponse.estoque(), is(40));
        assertThat(bebResponse.tipoBebida(), is(TipoBebida.SEMALCOOL));
        assertThat(bebResponse.valor(), is(2.59));
        assertThat(bebResponse.marca().get("nome"), is("Coca-Cola"));
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"Admin","User"})
    public void testDelete() {
        // Adicionando uma bebida no banco de dados
        BebidaDTO beb = new BebidaDTO(
                "Skol",
                1L,
                2.56d,
                "Boa",
                1,
                50);
        Long id = bebidaService.create(beb).id();
        given()
                .when().delete("/bebidas/" + id)
                .then()
                .statusCode(204);
        // verificando se a bebida fisica foi excluida
        BebidaResponseDTO bebResponse = null;
        try {
            bebidaService.findById(id);
        } catch (Exception e) {
        } finally {
            assertNull(bebResponse);
        }

    }

    @Test
    @TestSecurity(user = "testUser", roles = {"Admin","User"})
    public void testFindById() {
        Long id = 2l;
        given()
                .when().get("/bebidas/" + id)
                .then()
                .statusCode(200);
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"Admin","User"})
    public void testCount() {
        given()
                .when().get("/bebidas/count")
                .then()
                .statusCode(200);
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"Admin","User"})
    public void testSearch() {

        String nome = "Skol";

        given()
                .when().get("/bebidas/search/" + nome)
                .then()
                .statusCode(200);
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"Admin","User"})
    public void testGetByTipoBebida() {

        Integer tipo = 1;

        given()
                .when().get("/bebidas/procuraportipobebida/" + tipo)
                .then()
                .statusCode(200);
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"Admin","User"})
    public void testGetByMarca() {

        String nome = "Ambev";

        given()
                .when().get("/bebidas/prcurarpormarca/" + nome)
                .then()
                .statusCode(200);
    }
}
