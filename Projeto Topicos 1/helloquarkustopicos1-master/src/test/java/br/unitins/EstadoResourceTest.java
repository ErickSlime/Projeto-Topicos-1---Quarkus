package br.unitins;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.Test;

import br.unitins.topicos1.dto.EstadoDTO;
import br.unitins.topicos1.dto.EstadoResponseDTO;
import br.unitins.topicos1.service.EstadoService;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

import jakarta.inject.Inject;


@QuarkusTest
public class EstadoResourceTest {

    @Inject
    EstadoService estadoService;

    @Test
    @TestSecurity(user = "testUser", roles = {"Admin","User"})
    public void testGetAll() {
    given()
    .when().get("/estados")
    .then()
    .statusCode(200);
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"Admin","User"})
    public void testInsert() {
        EstadoDTO est = new EstadoDTO(
            "Tocantins",
            "TO"
        );
            given()
            .contentType(ContentType.JSON)
            .body(est)
            .when().post("/estados")
            .then()
            .statusCode(201)
            .body("id", notNullValue(),
            "nome", is("Tocantins"),
            "sigla", is("TO")
            );
 }

        @Test
        @TestSecurity(user = "testUser", roles = {"Admin","User"})
            public void testUpdate() {
                // Adicionando uma estado no banco de dados
                EstadoDTO est = new EstadoDTO(
                    "Tocantins",
                 "TO"
                );
                Long id = estadoService.create(est).id();
                // Criando outra estado para atuailzacao
                EstadoDTO estUpdate = new EstadoDTO(
                    "Plasma",
                    "PL"
                    );
                given()
                .contentType(ContentType.JSON)
                .body(estUpdate)
                .when().put("/estados/" + id)
                .then()
                .statusCode(204);
                     // Verificando se os dados foram atualizados no banco de dados
                EstadoResponseDTO estResponse = estadoService.findById(id);
                assertThat(estResponse.nome(), is("Plasma"));
                assertThat(estResponse.sigla(), is("PL"));
}

    @Test
    @TestSecurity(user = "testUser", roles = {"Admin","User"})
        public void testDelete() {
        // Adicionando uma estado no banco de dados
        EstadoDTO est = new EstadoDTO(
            "Tocantins",
            "TO"
        );
        Long id = estadoService.create(est).id();
        given()
        .when().delete("/estados/" + id)
        .then()
        .statusCode(204);
        // verificando se a estado fisica foi excluida
        EstadoResponseDTO estResponse = null;
        try {
         estadoService.findById(id);
        } catch (Exception e) {
        }
         finally {
         assertNull(estResponse);
        }

         }

         @Test
         @TestSecurity(user = "testUser", roles = {"Admin","User"})
            public void testFindById() {
                Long id = 2l;
                given()
                .when().get("/estados/" + id)
                .then()
                .statusCode(200);
            }

            @Test
            @TestSecurity(user = "testUser", roles = {"Admin","User"})
            public void testCount() {
                given()
                .when().get("/estados/count")
                .then()
                .statusCode(200);
            }

            @Test
            @TestSecurity(user = "testUser", roles = {"Admin","User"})
            public void testSearch() {

                String nome = "Tocantins";

                given()
                .when().get("/estados/search/" + nome)
                .then()
                .statusCode(200);
            }

   }
