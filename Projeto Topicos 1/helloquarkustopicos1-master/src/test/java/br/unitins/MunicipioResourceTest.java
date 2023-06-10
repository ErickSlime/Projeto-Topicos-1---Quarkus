package br.unitins;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.Test;

import br.unitins.topicos1.dto.MunicipioDTO;
import br.unitins.topicos1.dto.MunicipioResponseDTO;
import br.unitins.topicos1.service.MunicipioService;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

import jakarta.inject.Inject;


@QuarkusTest
public class MunicipioResourceTest {

    @Inject
    MunicipioService municipioService;

    @Test
    @TestSecurity(user = "testUser", roles = {"Admin","User"})
    public void testGetAll() {
    given()
    .when().get("/municipios")
    .then()
    .statusCode(200);
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"Admin","User"})
    public void testInsert() {
        MunicipioDTO mun = new MunicipioDTO(
        "Palmas",
        27l
        );
            given()
            .contentType(ContentType.JSON)
            .body(mun)
            .when().post("/municipios")
            .then()
            .statusCode(201)
            .body("id", notNullValue(),
            "nome", is("Palmas"),
            "estado.nome", is("Tocantins")
            );
 }
    @Test
    @TestSecurity(user = "testUser", roles = {"Admin","User"})
        public void testUpdate() {
        // Adicionando uma municipio no banco de dados
        MunicipioDTO mun = new MunicipioDTO(
            "Palmas",
            1l
        );
        Long id = municipioService.create(mun).id();
        // Criando outra municipio para atuailzacao
        MunicipioDTO munUpdate = new MunicipioDTO(
            "Rio",
            19l
        );
        given()
        .contentType(ContentType.JSON)
        .body(munUpdate)
        .when().put("/municipios/" + id)
        .then()
        .statusCode(204);
         // Verificando se os dados foram atualizados no banco de dados
        MunicipioResponseDTO munResponse = municipioService.findById(id);
        assertThat(munResponse.nome(), is("Rio"));
        assertThat(munResponse.estado().get("nome"), is("Rio de Janeiro"));
 }

    @Test
    @TestSecurity(user = "testUser", roles = {"Admin","User"})
        public void testDelete() {
        // Adicionando uma municipio no banco de dados
        MunicipioDTO mun = new MunicipioDTO(
            "Palmas",
            1l
        );
        Long id = municipioService.create(mun).id();
        given()
        .when().delete("/municipios/" + id)
        .then()
        .statusCode(204);
        // verificando se a municipio fisica foi excluida
        MunicipioResponseDTO munResponse = null;
        try {
         municipioService.findById(id);
        } catch (Exception e) {
        }
         finally {
         assertNull(munResponse);
        }

         }

         @Test
         @TestSecurity(user = "testUser", roles = {"Admin","User"})
            public void testFindById() {
                Long id = 2l;
                given()
                .when().get("/municipios/" + id)
                .then()
                .statusCode(200);
            }

            @Test
            @TestSecurity(user = "testUser", roles = {"Admin","User"})
            public void testCount() {
                given()
                .when().get("/municipios/count")
                .then()
                .statusCode(200);
            }

            @Test
            @TestSecurity(user = "testUser", roles = {"Admin","User"})
            public void testSearch() {

                String nome = "Palmas";

                given()
                .when().get("/municipios/search/" + nome)
                .then()
                .statusCode(200);
            }

   }
